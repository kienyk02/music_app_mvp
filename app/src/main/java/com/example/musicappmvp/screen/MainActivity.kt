package com.example.musicappmvp.screen

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicappmvp.R
import com.example.musicappmvp.data.dataSource.SongSourceImpl
import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.data.repository.SongRepositoryImpl
import com.example.musicappmvp.databinding.ActivityMainBinding
import com.example.musicappmvp.screen.listSong.adapter.SongAdapter
import com.example.musicappmvp.screen.listSong.presenter.SongContract
import com.example.musicappmvp.screen.listSong.presenter.SongPresenter
import com.example.musicappmvp.utils.MusicService

class MainActivity : AppCompatActivity(), SongContract.View {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val presenter: SongPresenter by lazy {
        SongPresenter(SongRepositoryImpl(SongSourceImpl()))
    }
    private var musicService: MusicService? = null
    private var isBound = false
    private var isUserSeeking = false
    private val adapter = SongAdapter(listOf())

    private val musicNotificationActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                when (intent.action) {
                    MusicService.ACTION_PLAY -> {
                        presenter.playSong()
                        binding.btnPlayPause.setImageResource(R.drawable.pause)
                    }

                    MusicService.ACTION_PAUSE -> {
                        presenter.pauseSong()
                        binding.btnPlayPause.setImageResource(R.drawable.play)
                    }

                    MusicService.ACTION_NEXT -> presenter.nextSong()
                    MusicService.ACTION_PREV -> presenter.previousSong()
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            isBound = true
            startSeekBarUpdater()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter.setView(this)
        presenter.getSongs()
        binding.apply {
            rvSongs.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rvSongs.adapter = adapter
            adapter.onItemClick = {
                presenter.selectSong(it)
            }
        }
        handleEvent()
    }

    private fun handleEvent() {
        binding.apply {
            btnPlayPause.setOnClickListener {
                if (btnPlayPause.isSelected) {
                    presenter.pauseSong()
                    btnPlayPause.isSelected = false
                    btnPlayPause.setImageResource(R.drawable.play)
                } else {
                    presenter.playSong()
                    btnPlayPause.isSelected = true
                    btnPlayPause.setImageResource(R.drawable.pause)
                }
            }

            btnPrevious.setOnClickListener {
                presenter.previousSong()
            }

            btnNext.setOnClickListener {
                presenter.nextSong()
            }

            skSong.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        musicService?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isUserSeeking = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isUserSeeking = false
                }
            })
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        val intentFilter = IntentFilter().apply {
            addAction(MusicService.ACTION_PLAY)
            addAction(MusicService.ACTION_PAUSE)
            addAction(MusicService.ACTION_NEXT)
            addAction(MusicService.ACTION_PREV)
        }
        registerReceiver(musicNotificationActionReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    override fun onGetSongsSuccess(songs: List<Song>) {
        adapter.setData(songs)
        binding.txtSongName.text = songs[0].name
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun startMusicService(song: Song) {
        musicService?.playSong(song)
    }

    override fun pauseMusicService(song: Song) {
        musicService?.pauseSong(song)
    }

    override fun changeSongService(song: Song) {
        musicService?.changeSong(song)
        binding.txtSongName.text = song.name
    }

    private fun startSeekBarUpdater() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (isBound && !isUserSeeking) {
                    musicService?.apply {
                        binding.skSong.max = getDuration()
                        binding.skSong.progress = getCurrentPosition()
                        mediaPlayer?.setOnCompletionListener {
                            mediaPlayer?.start()
                            presenter.nextSong()
                        }
                    }
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
}
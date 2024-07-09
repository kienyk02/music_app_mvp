package com.example.musicappmvp.utils

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.musicappmvp.data.model.Song


class MusicService : Service() {
    var mediaPlayer: MediaPlayer? = null
    private var currentSongResId: Int = -1
    private val binder = LocalBinder()
    private lateinit var notificationHelper: NotificationHelper

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    fun playSong(song: Song) {
        if (song.file != currentSongResId) {
            currentSongResId = song.file
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, currentSongResId).apply {
                start()
            }
        } else {
            mediaPlayer?.start()
        }
        notificationHelper.showNotification(song, true)
    }

    fun pauseSong(song: Song) {
        mediaPlayer?.pause()
        notificationHelper.showNotification(song, false)
    }

    fun changeSong(song: Song) {
        val isPlay = mediaPlayer?.isPlaying
        currentSongResId = song.file
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, currentSongResId)
        if (isPlay == true) {
            mediaPlayer?.start()
            notificationHelper.showNotification(song, true)
        } else {
            notificationHelper.showNotification(song, false)
        }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREV = "ACTION_PREV"
    }
}
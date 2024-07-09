package com.example.musicappmvp.screen.listSong.presenter

import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.data.repository.SongRepository

class SongPresenter(private val songRepository: SongRepository) : SongContract.Presenter {
    private var view: SongContract.View? = null
    private var currentSongIndex = 0
    var songs = listOf<Song>()
    fun setView(view: SongContract.View) {
        this.view = view
    }

    override fun getSongs() {
        songRepository.getSongs(object : OnResultListener<List<Song>> {
            override fun onSuccess(data: List<Song>) {
                songs = data
                view?.onGetSongsSuccess(data)
            }

            override fun onError(error: String) {
                view?.onError(error)
            }
        })
    }

    fun playSong() {
        view?.startMusicService(songs[currentSongIndex])
    }

    fun pauseSong() {
        view?.pauseMusicService(songs[currentSongIndex])
    }

    fun nextSong() {
        if (songs.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songs.size
            view?.changeSongService(songs[currentSongIndex])
        }
    }

    fun previousSong() {
        if (songs.isNotEmpty()) {
            currentSongIndex =
                if (currentSongIndex - 1 < 0) songs.size - 1 else currentSongIndex - 1
            view?.changeSongService(songs[currentSongIndex])
        }
    }

    fun selectSong(position: Int) {
        currentSongIndex = position
        view?.changeSongService(songs[currentSongIndex])
    }
}
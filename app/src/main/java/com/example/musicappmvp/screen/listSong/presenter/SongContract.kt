package com.example.musicappmvp.screen.listSong.presenter

import com.example.musicappmvp.data.model.Song

interface SongContract {
    interface Presenter{
        fun getSongs()
    }

    interface View{
        fun onGetSongsSuccess(songs: List<Song>)
        fun onError(error: String)
        fun startMusicService(song: Song)
        fun pauseMusicService(song: Song)
        fun changeSongService(song: Song)
    }

}
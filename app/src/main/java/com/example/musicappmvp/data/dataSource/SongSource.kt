package com.example.musicappmvp.data.dataSource

import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.screen.listSong.presenter.OnResultListener

interface SongSource {
    fun getSongs(listener: OnResultListener<List<Song>>)
}
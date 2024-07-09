package com.example.musicappmvp.data.repository

import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.screen.listSong.presenter.OnResultListener

interface SongRepository {
    fun getSongs(listener: OnResultListener<List<Song>>)
}
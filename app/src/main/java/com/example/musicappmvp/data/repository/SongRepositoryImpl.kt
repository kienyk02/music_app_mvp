package com.example.musicappmvp.data.repository

import com.example.musicappmvp.data.dataSource.SongSource
import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.screen.listSong.presenter.OnResultListener

class SongRepositoryImpl(private val songSource: SongSource): SongRepository {
    override fun getSongs(listener: OnResultListener<List<Song>>) {
        songSource.getSongs(listener)
    }
}
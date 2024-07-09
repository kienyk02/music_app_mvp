package com.example.musicappmvp.screen.listSong.presenter

interface OnResultListener<T> {
    fun onSuccess(data: T)
    fun onError(error: String)
}
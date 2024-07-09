package com.example.musicappmvp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val mainActivityIntent = Intent(intent.action)
            context.sendBroadcast(mainActivityIntent)
        }
    }
}

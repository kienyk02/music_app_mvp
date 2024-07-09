package com.example.musicappmvp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicappmvp.R
import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.screen.MainActivity

class NotificationHelper(private val context: Context) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for music player notifications"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(song: Song, isPlaying: Boolean) {
        val notificationLayout = RemoteViews(context.packageName, R.layout.custom_notification)

        notificationLayout.setTextViewText(R.id.txtSongName, song.name)
        notificationLayout.setTextViewText(R.id.txtSongAuthor, song.author)
        notificationLayout.setImageViewResource(
            R.id.btnPlayPause,
            if (isPlaying) R.drawable.pause else R.drawable.play
        )

        val playPauseIntent = Intent(context, MusicReceiver::class.java).apply {
            action = if (isPlaying) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY
        }
        val playPausePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextIntent = Intent(context, MusicReceiver::class.java).apply {
            action = MusicService.ACTION_NEXT
        }
        val nextPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val previousIntent = Intent(context, MusicReceiver::class.java).apply {
            action = MusicService.ACTION_PREV
        }
        val previousPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            previousIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationLayout.setOnClickPendingIntent(R.id.btnPlayPause, playPausePendingIntent)
        notificationLayout.setOnClickPendingIntent(R.id.btnNext, nextPendingIntent)
        notificationLayout.setOnClickPendingIntent(R.id.btnPrevious, previousPendingIntent)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP  or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContent(notificationLayout)
//            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .build()

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    fun cancelNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)
    }

    companion object {
        const val CHANNEL_ID = "music_player_channel"
        const val CHANNEL_NAME = "Music Player"
    }
}

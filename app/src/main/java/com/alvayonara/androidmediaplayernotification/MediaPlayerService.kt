package com.alvayonara.androidmediaplayernotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.Rating
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MediaPlayerService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "audio channel"

        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_REWIND = "action_rewind"
        const val ACTION_FAST_FORWARD = "action_fast_foward"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"
    }

    private var mMediaPlayer: MediaPlayer? = null
    private var mManager: MediaSessionManager? = null
    private var mSession: MediaSession? = null
    private var mController: MediaController? = null

    override fun onBind(intent: Intent?): IBinder? = null

    private fun handleIntent(intent: Intent?) {
        if (intent == null || intent.action == null) return
        when (intent.action) {
            ACTION_PLAY -> mController!!.transportControls.play()
            ACTION_PAUSE -> mController!!.transportControls.pause()
            ACTION_FAST_FORWARD -> mController!!.transportControls.fastForward()
            ACTION_REWIND -> mController!!.transportControls.rewind()
            ACTION_PREVIOUS -> mController!!.transportControls.skipToPrevious()
            ACTION_NEXT -> mController!!.transportControls.skipToNext()
            ACTION_STOP -> mController!!.transportControls.stop()
        }
    }

    private fun generateAction(
        icon: Int,
        title: String,
        intentAction: String
    ): NotificationCompat.Action {
        val intent = Intent(applicationContext, MediaPlayerService::class.java)
        intent.action = intentAction
        val pendingIntent = PendingIntent.getService(applicationContext, 1, intent, 0)
        return NotificationCompat.Action.Builder(icon, title, pendingIntent).build()
    }

    private fun buildNotification(action: NotificationCompat.Action) {
        val style = androidx.media.app.NotificationCompat.MediaStyle()
        val intent = Intent(applicationContext, MediaPlayerService::class.java)
        intent.action = ACTION_STOP
        val pendingIntent = PendingIntent.getService(applicationContext, 1, intent, 0)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Media Title")
            .setContentText("Media Artist")
            .setDeleteIntent(pendingIntent)
            .setStyle(style)
            .addAction(generateAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                ACTION_PREVIOUS
            )
        )
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND))
        builder.addAction(action)
        builder.addAction(
            generateAction(
                android.R.drawable.ic_media_ff,
                "Fast Foward",
                ACTION_FAST_FORWARD
            )
        )
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT))
        style.setShowActionsInCompactView(0, 1, 2, 3, 4)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_NAME
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mManager == null) {
            initMediaSessions()
        }
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initMediaSessions() {
        mMediaPlayer = MediaPlayer()
        mSession = MediaSession(applicationContext, "simple player session")
        mController = MediaController(applicationContext, mSession!!.sessionToken)
        mSession!!.setCallback(object : MediaSession.Callback() {
            override fun onPlay() {
                super.onPlay()
                Log.e("MediaPlayerService", "onPlay")
                buildNotification(
                    generateAction(
                        android.R.drawable.ic_media_pause,
                        "Pause",
                        ACTION_PAUSE
                    )
                )
            }

            override fun onPause() {
                super.onPause()
                Log.e("MediaPlayerService", "onPause")
                buildNotification(
                    generateAction(
                        android.R.drawable.ic_media_play,
                        "Play",
                        ACTION_PLAY
                    )
                )
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                Log.e("MediaPlayerService", "onSkipToNext")
                //Change media here
                buildNotification(
                    generateAction(
                        android.R.drawable.ic_media_pause,
                        "Pause",
                        ACTION_PAUSE
                    )
                )
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                Log.e("MediaPlayerService", "onSkipToPrevious")
                //Change media here
                buildNotification(
                    generateAction(
                        android.R.drawable.ic_media_pause,
                        "Pause",
                        ACTION_PAUSE
                    )
                )
            }

            override fun onFastForward() {
                super.onFastForward()
                Log.e("MediaPlayerService", "onFastForward")
                //Manipulate current media here
            }

            override fun onRewind() {
                super.onRewind()
                Log.e("MediaPlayerService", "onRewind")
                //Manipulate current media here
            }

            override fun onStop() {
                super.onStop()
                Log.e("MediaPlayerService", "onStop")
                //Stop media player here
                val notificationManager =
                    applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(1)
                val intent = Intent(applicationContext, MediaPlayerService::class.java)
                stopService(intent)
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
            }

            override fun onSetRating(rating: Rating) {
                super.onSetRating(rating)
            }
        })
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mSession!!.release()
        return super.onUnbind(intent)
    }
}
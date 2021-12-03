package com.alvayonara.androidmediaplayernotification

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.alvayonara.androidmediaplayernotification.model.AudioPlayerEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.IOException


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

        const val EXTRA_CURRENT_PLAYER = "extra_current_player"
        const val EXTRA_PLAYLIST = "extra_playlist"
    }

    private var mMediaPlayer: MediaPlayer = MediaPlayerSingleton.getInstance()
    private var mSession: MediaSession? = null
    private var mController: MediaController? = null

    private var _audioPlayerEntity: AudioPlayerEntity? = null
    private var _itemsAudio: List<AudioPlayerEntity> = ArrayList()
    private var _currentSongIndex = 0

    override fun onBind(intent: Intent?): IBinder? = null

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        intent.extras?.let {
            _audioPlayerEntity = it.getParcelable(EXTRA_CURRENT_PLAYER)
            _itemsAudio = it.getParcelableArrayList(EXTRA_PLAYLIST) ?: emptyList()
        }
    }

    private fun handleAction(intent: Intent?) {
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
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setSmallIcon(R.drawable.ic_media_play)
            .setContentTitle(_audioPlayerEntity?.title.orEmpty())
            .setContentText(_audioPlayerEntity?.terms.orEmpty())
            .setDeleteIntent(pendingIntent)
            .setStyle(style)
            .addAction(generateAction(
                R.drawable.ic_media_previous,
                "Previous",
                ACTION_PREVIOUS
            )
        )
        builder.addAction(generateAction(R.drawable.ic_media_rew, "Rewind", ACTION_REWIND))
        builder.addAction(action)
        builder.addAction(
            generateAction(
                R.drawable.ic_media_ff,
                "Fast Foward",
                ACTION_FAST_FORWARD
            )
        )
        builder.addAction(generateAction(R.drawable.ic_media_next, "Next", ACTION_NEXT))
        style.setShowActionsInCompactView(0, 1, 2, 3, 4)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_NAME
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        Glide.with(this)
            .asBitmap()
            .load(_audioPlayerEntity?.thumbnail)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    builder.setLargeIcon(resource)
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        if (mSession == null) {
            initMediaSessions()
            _audioPlayerEntity?.let {
                playAudio(it)
            }
        }
        handleAction(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initMediaSessions() {
        mSession = MediaSession(applicationContext, "simple player session")
        mController = MediaController(applicationContext, mSession!!.sessionToken)
        mSession!!.setCallback(object : MediaSession.Callback() {
            override fun onPlay() {
                super.onPlay()
                Log.e("MediaPlayerService", "onPlay")
                play()
            }

            override fun onPause() {
                super.onPause()
                Log.e("MediaPlayerService", "onPause")
                pause()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                Log.e("MediaPlayerService", "onSkipToNext")
                buildNotification(
                    generateAction(
                        R.drawable.ic_media_pause,
                        "Pause",
                        ACTION_PAUSE
                    )
                )
                next()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                Log.e("MediaPlayerService", "onSkipToPrevious")
                buildNotification(
                    generateAction(
                        R.drawable.ic_media_pause,
                        "Pause",
                        ACTION_PAUSE
                    )
                )
                prev()
            }

            override fun onFastForward() {
                super.onFastForward()
                Log.e("MediaPlayerService", "onFastForward")
            }

            override fun onRewind() {
                super.onRewind()
                Log.e("MediaPlayerService", "onRewind")
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
        })
    }

    private fun next() {
        if (_currentSongIndex < _itemsAudio.size - 1) {
            _currentSongIndex += 1
            playAudio(_itemsAudio[_currentSongIndex])
        } else {
            _currentSongIndex = 0
            playAudio(_itemsAudio[_currentSongIndex])
        }
    }

    private fun prev() {
        if (_currentSongIndex > 0) {
            _currentSongIndex -= 1
            playAudio(_itemsAudio[_currentSongIndex])
        } else {
            // play last song
            _currentSongIndex = 0
            playAudio(_itemsAudio[_currentSongIndex])
        }
    }

    private fun playAudio(audioPlayerEntity: AudioPlayerEntity) {
        try {
            mMediaPlayer.apply {
                _audioPlayerEntity = audioPlayerEntity

                reset()
                setDataSource(audioPlayerEntity.audio)
                prepareAsync()

                setOnPreparedListener { mediaPlayer ->
                    buildNotification(
                        generateAction(
                            R.drawable.ic_media_pause,
                            "Pause",
                            ACTION_PAUSE
                        )
                    )
                    mediaPlayer.start()
                }
            }

            // set new current song index
            _currentSongIndex = _itemsAudio.indexOf(audioPlayerEntity)

//            populateDataAudioPlayer(audioPlayerEntity)

//            _adapter.setNameArticle(audioPlayerEntity.name)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun play() {
        try {
            buildNotification(
                generateAction(
                    R.drawable.ic_media_pause,
                    "Pause",
                    ACTION_PAUSE
                )
            )
            if (!mMediaPlayer.isPlaying) {
                mMediaPlayer.seekTo(mMediaPlayer.currentPosition)
                mMediaPlayer.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pause() {
        try {
            buildNotification(
                generateAction(
                    R.drawable.ic_media_play,
                    "Play",
                    ACTION_PLAY
                )
            )
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mSession!!.release()
        return super.onUnbind(intent)
    }
}
package com.alvayonara.androidmediaplayernotification

import android.media.AudioAttributes
import android.media.MediaPlayer

class MediaPlayerSingleton private constructor() {

    companion object {
        private var single: MediaPlayer = MediaPlayer()

        fun getInstance(): MediaPlayer {
            if (single == null)
                single = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                }
            return single
        }
    }
}
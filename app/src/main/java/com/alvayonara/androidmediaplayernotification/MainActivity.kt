package com.alvayonara.androidmediaplayernotification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Navigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AudioPlayerFragment.newInstance())
                .commitNow()
        }
    }

    override fun toAudioPlayer() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AudioPlayerFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
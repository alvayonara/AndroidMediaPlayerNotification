package com.alvayonara.androidmediaplayernotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(applicationContext, MediaPlayerService::class.java)
        intent.action = MediaPlayerService.ACTION_PLAY
        startService(intent)
    }
}
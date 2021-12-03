package com.alvayonara.androidmediaplayernotification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class AudioPlayerFragment : Fragment(R.layout.fragment_audio_player) {

    companion object {
        fun newInstance(): AudioPlayerFragment = AudioPlayerFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireActivity(), MediaPlayerService::class.java).apply {
            putExtra(MediaPlayerService.EXTRA_CURRENT_PLAYER, DataDummy.getLocalAudio()[0])
            putParcelableArrayListExtra(MediaPlayerService.EXTRA_PLAYLIST, ArrayList(DataDummy.getLocalAudio()))
        }
        requireActivity().startService(intent)
    }
}
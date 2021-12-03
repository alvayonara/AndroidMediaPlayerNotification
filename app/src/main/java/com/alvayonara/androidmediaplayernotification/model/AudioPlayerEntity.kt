package com.alvayonara.androidmediaplayernotification.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioPlayerEntity(
    val name: String,
    val title: String,
    val publishedDate: String,
    val terms: String,
    val thumbnail: String,
    var event: String,
    val audio: String,
    val slug: String,
    val isBebasAkses: Boolean,
    val subscriptionStatus: Int,
    val cursor: String = "",
    val id: String = "",
    val authorName: String = "",
    val contentCategory: String = "",
    val articleTags: String = ""
) : Parcelable

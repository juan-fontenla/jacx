package com.apm.jacx.spotify.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistItem(
    val id: String,
    val imageUri: String,
    val name: String,
    val totalTracks: Int,
) : Parcelable
package com.apm.jacx.spotify

import java.util.*

data class TrackItem(
    val id: String,
    val uri: String,
    val name: String,
    val artists: String,
    val explicit: Boolean,
    val duration: Int,
    val previewUrl: String?,
    val addedAt: Date
)
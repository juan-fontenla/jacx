package com.apm.jacx.spotify.domain

import java.text.SimpleDateFormat
import com.spotify.protocol.types.Artist
import java.util.*

data class Track(
    val artists: List<Artist>,
    val duration_ms: Int,
    val explicit: Boolean,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: String?,
    val uri: String
) {

    fun toTrackItem(addedAt: String = ""): TrackItem =
        TrackItem(
            id,
            uri,
            name,
            artists.joinToString(", ") { it.name },
            explicit,
            duration_ms,
            preview_url,
            if (addedAt == "") Date() else SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.US
            ).parse(addedAt)!!
        )
}
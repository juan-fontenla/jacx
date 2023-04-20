package com.apm.jacx.spotify.response

import com.apm.jacx.spotify.Track
import com.apm.jacx.spotify.TrackItem


data class PlaylistTracksResponse(
    val items: List<TrackResponse>,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
) {

    fun toTrackItems(): List<TrackItem> {
        val tracks = mutableListOf<TrackItem>()
        items.forEach {
            tracks.add(it.toTrackItem())
        }
        return tracks
    }

    data class TrackResponse(
        val added_at: String,
        val track: Track
    ) {

        fun toTrackItem(): TrackItem =
            track.toTrackItem(added_at)
    }
}
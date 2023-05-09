package com.apm.jacx.spotify

import com.apm.jacx.spotify.domain.PlaylistItem
import com.apm.jacx.spotify.response.PlaylistTracksResponse


data class PlayList (
    val id: String,
    val images: List<Image>,
    val name: String,
    // Permite obtener las canciones dentro de la playlist
    val tracks: PlaylistTracksResponse
) {

    fun toPlaylistItem(): PlaylistItem {
        return PlaylistItem(
            id,
            if (images.isEmpty()) "" else images.first().url,
            name,
            tracks.total
        )
    }
}

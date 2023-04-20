package com.apm.jacx.spotify

import com.apm.jacx.spotify.response.PlaylistTracksResponse


data class PlayList (
    val id: String,
    val images: List<Image>,
    val name: String,
    // Permite obtener las canciones dentro de la playlist
    val tracks: PlaylistTracksResponse
)

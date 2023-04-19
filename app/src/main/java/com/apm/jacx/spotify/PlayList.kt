package com.apm.jacx.spotify


data class PlayList (
    val id: String,
    val images: List<Image>,
    val name: String
//    val tracks: PlaylistTracksResponse para obtener las canciones dentro de la playlist
)

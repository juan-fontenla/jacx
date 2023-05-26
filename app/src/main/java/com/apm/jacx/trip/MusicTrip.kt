package com.apm.jacx.trip

data class MusicTrip(
    val spotifyId: String,
    val tituloCancion: String,
    val autorCancion: String,
    val rutaName: String
)

data class RoutePlaylist(
    val playlists: Array<PlaylistID>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoutePlaylist

        if (!playlists.contentEquals(other.playlists)) return false

        return true
    }

    override fun hashCode(): Int {
        return playlists.contentHashCode()
    }
}

data class PlaylistID(
    val spotifyId: String
)

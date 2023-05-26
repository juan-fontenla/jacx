package com.apm.jacx.spotify.response

import com.spotify.protocol.types.Artist

data class SongResponse(
    val name: String,
    val artists: Array<Artist>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongResponse

        if (name != other.name) return false
        if (!artists.contentEquals(other.artists)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + artists.contentHashCode()
        return result
    }
}

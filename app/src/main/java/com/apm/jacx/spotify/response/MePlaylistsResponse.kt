package com.apm.jacx.spotify.response

import com.apm.jacx.spotify.PlayList

data class MePlaylistsResponse(
    val items: List<PlayList>
)
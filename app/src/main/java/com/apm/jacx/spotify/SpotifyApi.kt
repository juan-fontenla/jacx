package com.apm.jacx.spotify

import retrofit2.http.GET

interface SpotifyApi {

    @GET("me/playlists")
    suspend fun getMePlaylists(): Result<MePlaylistsResponse>

}
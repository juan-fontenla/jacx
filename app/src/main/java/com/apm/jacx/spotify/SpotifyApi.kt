package com.apm.jacx.spotify

import com.apm.jacx.spotify.response.PlaylistTracksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("me/playlists")
    suspend fun getMePlaylists(): Result<MePlaylistsResponse>

    // Es necesario para reconocer la playList que estamos clickando.
//    @GET("playlists/{playlist_id}")
//    suspend fun getPlaylistById(
//        @Path("playlist_id") playlistId: String
//    ): Response<PlayList>

    // Recuperamos las canciones correspondientes a esta playlis_id
    @GET("playlists/{id}/tracks")
    suspend fun getPlaylistTracks(
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Result<PlaylistTracksResponse>


}
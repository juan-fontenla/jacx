package com.apm.jacx.spotify.domain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apm.jacx.spotify.SpotifyApi
import com.apm.jacx.spotify.TrackItem
import com.apm.jacx.spotify.adapter.ResultCallAdapterFactory
import com.apm.jacx.spotify.domain.PlaylistItem
import com.apm.jacx.spotify.domain.SortType
import com.apm.jacx.spotify.interceptor.AuthorizationInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// TODO: Revisar porque creo que esta clase sobra.
class PlaylistTracksViewModel constructor(
    private val playlistItem: PlaylistItem,
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Int>()
    val tracks = MutableLiveData<List<TrackItem>>()
    var sortType: SortType = SortType.CUSTOM

    private var ascending: Boolean = true
    private lateinit var originalTracklist: List<TrackItem>
    private lateinit var tracklist: MutableList<TrackItem>

    init {
        loading.value = true
        viewModelScope.launch {
            try {
                originalTracklist = getPlaylistTracks(playlistItem.id)
                Log.d("", originalTracklist.toString())
                sort(null, null)
                loading.value = false
            } catch (e: HttpException) {
               // error.value = R.string.error
            } catch (e: IOException) {
//                error.value = R.string.internet
            }
        }
    }

    suspend fun getPlaylistTracks(playlistId: String): List<TrackItem> {
        val tracks = mutableListOf<TrackItem>()

        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthorizationInterceptor())
            .build()

        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .serializeNulls()
            .setLenient()
            .create()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)
            .build()

        val spotifyApi = retrofit.create(SpotifyApi::class.java)
        val limit = 100
        var offset = 0

        do {
            val response = spotifyApi.getPlaylistTracks(playlistId, limit, offset).getOrThrow()
            tracks.addAll(response.toTrackItems())
            offset += limit
        } while (response.next != null)


        return tracks
    }

    fun sort(sortType: SortType?, ascending: Boolean?) {
        if (sortType != null) {
            this.sortType = sortType
        }
        if (ascending != null) {
            this.ascending = ascending
        }

        when (this.sortType) {
            SortType.TITLE -> if (this.ascending) {
                tracklist.sortBy { it.name }
            } else {
                tracklist.sortByDescending { it.name }
            }
            SortType.ARTIST -> if (this.ascending) {
                tracklist.sortBy { it.artists }
            } else {
                tracklist.sortByDescending { it.artists }
            }
            SortType.DURATION -> if (this.ascending) {
                tracklist.sortBy { it.duration }
            } else {
                tracklist.sortByDescending { it.duration }
            }
            SortType.RECENTLY_ADDED -> if (this.ascending) {
                tracklist.sortBy { it.addedAt }
            } else {
                tracklist.sortByDescending { it.addedAt }
            }
            SortType.CUSTOM -> if (this.ascending) {
                tracklist = originalTracklist.toMutableList()
            } else {
                tracklist = originalTracklist.toMutableList()
                tracklist.reverse()
            }
        }
        tracks.value = tracklist.toList()
//        println(tracks)
    }
}
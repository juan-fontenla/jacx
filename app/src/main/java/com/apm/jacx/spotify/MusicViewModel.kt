package com.apm.jacx.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.spotify.adapter.ResultCallAdapterFactory
import com.apm.jacx.spotify.domain.PlayList
import com.apm.jacx.spotify.domain.TrackItem
import com.apm.jacx.spotify.interceptor.AuthorizationInterceptor
import com.apm.jacx.spotify.response.MeResponse
import com.apm.jacx.trip.MusicTrip
import com.apm.jacx.trip.PlaylistID
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MusicViewModel : ViewModel() {
    val tracks = MutableLiveData<List<TrackItem>>()
    val playList = MutableLiveData<List<PlayList>>()
    val me = MutableLiveData<MeResponse>()
    val listTracksTrip = MutableLiveData<List<MusicTrip>>()

    private var spotifyApi: SpotifyApi;

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )

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

        spotifyApi = retrofit.create(SpotifyApi::class.java)
    }

    fun initPlaylistMutableData() {
        viewModelScope.launch {
            try {
                playList.value = spotifyApi.getMePlaylists().getOrThrow().items
            } catch (e: HttpException) {
                Log.d("Spotify API", "HTTP fallo")
            } catch (e: IOException) {
                Log.d("Spotify API", "No hay conexión a internet")
            }
        }
    }

    fun initTracksMutableData(playlistId: String) {
        viewModelScope.launch {
            try {
                tracks.value =
                    spotifyApi.getPlaylistTracks(playlistId, 100, 0).getOrThrow().toTrackItems()
            } catch (e: HttpException) {
                Log.d("Spotify API", "HTTP fallo")
            } catch (e: IOException) {
                Log.d("Spotify API", "No hay conexión a internet")
            }
        }
    }

    fun initTracksByArrayIds(spotifyIDs: Array<PlaylistID>, rutaName: String) {
        viewModelScope.launch {
            try {
                val listTracks = mutableListOf<MusicTrip>()
                spotifyIDs.forEach { el ->
                    val song = spotifyApi.getTracksById(el.spotifyId).getOrThrow()
                    listTracks.add(MusicTrip(el.spotifyId, song.name, song.artists[0].name, rutaName))
                }
                listTracksTrip.value = listTracks
            } catch (e: HttpException) {
                Log.d("Spotify API", "HTTP fallo")
            } catch (e: IOException) {
                Log.d("Spotify API", "No hay conexión a internet")
            }

        }
    }

    fun getSpotifyUserInformation() {
        viewModelScope.launch {
            try {
                me.value = spotifyApi.getUserInformation().getOrThrow()
            } catch (e: HttpException) {
                Log.d("Spotify API", "HTTP fallo")
            } catch (e: IOException) {
                Log.d("Spotify API", "No hay conexión a internet")
            }
        }
    }

}
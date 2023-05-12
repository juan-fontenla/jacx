package com.apm.jacx.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apm.jacx.spotify.adapter.ResultCallAdapterFactory
import com.apm.jacx.spotify.domain.PlaylistItem
import com.apm.jacx.spotify.interceptor.AuthorizationInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


// Recupera las playList de un usuario
class MusicViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val tracks = MutableLiveData<List<TrackItem>>()
    val playList = MutableLiveData<List<PlayList>>()

    init {
        loading.value = true
        viewModelScope.launch {
            try {
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

                playList.value = spotifyApi.getMePlaylists().getOrThrow().items

                // TODO: Muestra por consola el valor de los diferentes items (previewUrl)
                // Obtiene los valores de las diferentes tracks de una playlist
                playList.value!!.forEach { p ->
                    run {
                        tracks.value = spotifyApi.getPlaylistTracks(p.id, 100, 0).getOrThrow().toTrackItems()
                    }
                }

                loading.value = false
            } catch (e: HttpException) {
                error.value = "HTTP fallo"
            } catch (e: IOException) {
                error.value = "No hay conexión a internet"
            }
        }
    }

}
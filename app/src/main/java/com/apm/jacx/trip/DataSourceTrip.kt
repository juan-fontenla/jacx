package com.apm.jacx.trip

import android.util.Log
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class DataSourceTrip {

    fun loadFriendsTrip(): List<Friend> {
        val list = mutableListOf<Friend>()
        val prueba = getFriends()

        for (i in 1..50) {
            println(prueba)
//            val num = list.size;
//            list.add(
//                Friend(num, R.mipmap.ic_launcher_round,"Nombre Completo","xxxx@udc.es")
//            )
        }
        return list
    }

    private fun getFriends() {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responsePost = ApiClient.get("/friends")
                val jsonObject: JsonObject = Gson().fromJson(responsePost, JsonObject::class.java)
                print("SON UN TOTAL DE "+jsonObject.size())
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
            }
        }
    }

    fun loadMusicTrip(): List<MusicTrip> {
        val list = mutableListOf<MusicTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(MusicTrip(num,"Calm Down","Rema, Selena Gomez", R.drawable.baseline_photo_album_24))
        }
        return list
    }

    fun loadAlbumTrip(): List<AlbumTrip> {
        val list = mutableListOf<AlbumTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(AlbumTrip(num,"UserName",2,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))
        }
        return list
    }
}

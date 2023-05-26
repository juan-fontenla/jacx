package com.apm.jacx.trip

import android.util.Log
import androidx.fragment.app.viewModels
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
import com.apm.jacx.spotify.MusicViewModel
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class DataSourceTrip {

    suspend fun getFriends(): List<Friend> {
        val listFriends: MutableList<Friend> = mutableListOf()
        val responseGet = ApiClient.get("/friends")
        Log.d("GET FRIENDS", responseGet)
        val friendsArray = Gson().fromJson(responseGet, Array<FriendUsername>::class.java)
        friendsArray.forEach { el ->
            val username = el.usernameFriend
            val userData = ApiClient.get("/user/$username")
            val jsonData: JsonObject = Gson().fromJson(userData, JsonObject::class.java)
            val friend =
                Friend(jsonData.get("username").asString, jsonData.get("email").asString)
            listFriends.add(friend)
        }
        Log.d("LISTA DE AMIGOS", listFriends.toString())
        return listFriends.toList()
    }

    fun loadAlbumTrip(): List<AlbumTrip> {
        val list = mutableListOf<AlbumTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(
                AlbumTrip(
                    num,
                    "UserName",
                    2,
                    R.mipmap.ic_launcher_round,
                    R.drawable.baseline_photo_album_24
                )
            )
        }
        return list
    }
}

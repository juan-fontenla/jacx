package com.apm.jacx.trip


import android.util.Log
import androidx.fragment.app.viewModels
import com.apm.jacx.client.ApiClient
import com.apm.jacx.spotify.MusicViewModel
import com.google.android.gms.common.api.Api
import com.apm.jacx.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

class DataSourceTrip {

    fun getFriends(jsonArray:JsonArray): List<FriendUsername> {

        val gson = Gson()
        val friendsList = object : TypeToken<List<FriendUsername>>() {}.type

        return gson.fromJson(jsonArray, friendsList)
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

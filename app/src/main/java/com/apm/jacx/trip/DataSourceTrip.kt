package com.apm.jacx.trip

import com.apm.jacx.R
import com.apm.jacx.model.Photo
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

class DataSourceTrip {

    fun getFriends(jsonArray:JsonArray): List<FriendUsername> {

        val gson = Gson()
        val friendsList = object : TypeToken<List<FriendUsername>>() {}.type

        return gson.fromJson(jsonArray, friendsList)
    }

    fun loadMusicTrip(): List<MusicTrip> {
        val list = mutableListOf<MusicTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(
                MusicTrip(
                    num,
                    "Calm Down",
                    "Rema, Selena Gomez",
                    R.drawable.baseline_photo_album_24
                )
            )
        }
        return list
    }

    fun loadAlbumTrip(jsonArray: JsonArray): List<Photo> {

        val gson = Gson()
        val albumList = object : TypeToken<List<Photo>>() {}.type

        return gson.fromJson(jsonArray, albumList)
    }
}

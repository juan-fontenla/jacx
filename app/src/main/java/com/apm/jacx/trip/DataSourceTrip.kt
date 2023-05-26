package com.apm.jacx.trip


import android.util.Log
import androidx.fragment.app.viewModels
import com.apm.jacx.client.ApiClient
import com.apm.jacx.spotify.MusicViewModel
import com.google.android.gms.common.api.Api
import com.apm.jacx.R
import com.apm.jacx.model.Photo
import com.apm.jacx.model.User
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

class DataSourceTrip {

    fun getFriends(jsonArray:JsonArray): List<User> {

        val gson = Gson()
        val friendsList = object : TypeToken<List<User>>() {}.type

        return gson.fromJson(jsonArray, friendsList)
    }

    fun loadAlbumTrip(jsonArray: JsonArray): List<Photo> {

        val gson = Gson()
        val albumList = object : TypeToken<List<Photo>>() {}.type

        return gson.fromJson(jsonArray, albumList)
    }
}

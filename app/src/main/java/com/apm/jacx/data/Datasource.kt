package com.apm.jacx.data

import com.apm.jacx.model.Photo
import com.apm.jacx.model.Trip
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Datasource {
    fun loadPhotos(jsonArray: JsonArray): List<Photo> {
        val gson = Gson()
        val photoList = object : TypeToken<List<Photo>>() {}.type

        return gson.fromJson(jsonArray, photoList)
    }

    fun loadTrips(): List<Trip> {
        val list = mutableListOf<Trip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(Trip(num, "viaje $num"))
        }
        return list
    }
}
package com.apm.jacx.data

import com.apm.jacx.model.Photo
import com.apm.jacx.model.Trip
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class Datasource {
    fun loadPhotos(jsonArray: JsonArray): List<Photo> {
        val gson = Gson()
        val photoList = object : TypeToken<List<Photo>>() {}.type

        return gson.fromJson(jsonArray, photoList)
    }

    fun loadTrips(): List<Trip> {
        val list = mutableListOf<Trip>()
        for (i in 1..50) {
            val json = JSONObject()
            json.put("id", i)
            json.put("title", "viaje $i")
            list.add(Trip(json))
        }
        return list
    }
}
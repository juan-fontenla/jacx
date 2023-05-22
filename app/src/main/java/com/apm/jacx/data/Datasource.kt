package com.apm.jacx.data

import com.apm.jacx.model.Photo
import com.apm.jacx.model.Trip
import org.json.JSONObject

class Datasource {
    fun loadPhotos(): List<Photo> {
        val list = mutableListOf<Photo>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(Photo(num))
        }
        return list
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
package com.apm.jacx.data

import com.apm.jacx.model.Photo
import com.apm.jacx.model.Trip

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
            val num: Int = list.size;
            list.add(Trip(num, "viaje $num"))
        }
        return list
    }
}
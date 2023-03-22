package com.apm.jacx.data

import com.apm.jacx.model.Photo
import com.apm.jacx.model.Song
import com.apm.jacx.model.Trip

class Datasource {

    fun loadSongs(): List<Song> {
        val list = mutableListOf<Song>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(Song(num, "canción ${list.size}", "artista ${list.size}", 0))
        }
        return list
    }

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
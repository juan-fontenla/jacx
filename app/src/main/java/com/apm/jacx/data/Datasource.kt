package com.apm.jacx.data

import com.apm.jacx.model.Song

class Datasource {

    fun loadSongs(): List<Song> {
        val list = mutableListOf<Song>()
        for(i in 1..50) {
            val num : Int = list.size;
            list.add(Song(num, "canción ${list.size}", "artista ${list.size}", 0))
        }
        return list
    }
}
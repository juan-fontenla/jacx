package com.apm.jacx.trip

import com.apm.jacx.R

class DataSourceTrip {

    fun loadFriendsTrip(): List<Friend> {
        val list = mutableListOf<Friend>()
        for (i in 1..50) {
            val num = list.size;
            list.add(
                Friend(num, R.mipmap.ic_launcher_round,"Nombre Completo","xxxx@udc.es")
            )
        }
        return list
    }

    fun loadMusicTrip(): List<MusicTrip> {
        val list = mutableListOf<MusicTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(MusicTrip(num,"Calm Down","Rema, Selena Gomez", R.drawable.baseline_photo_album_24))
        }
        return list
    }

    fun loadAlbumTrip(): List<AlbumTrip> {
        val list = mutableListOf<AlbumTrip>()
        for (i in 1..50) {
            val num: Int = list.size;
            list.add(AlbumTrip(num,"UserName",2,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))
        }
        return list
    }
}

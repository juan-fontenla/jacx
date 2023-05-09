package com.apm.jacx.spotify.domain

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteItem(
    val id: String,
    val title: String,
    val playlistId: String,
    var firstLat: Double,
    var firstLong: Double,
    val coordinates: List<LatLng>,
    val creator: String
) : Parcelable
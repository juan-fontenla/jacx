package com.apm.jacx.util

import com.apm.jacx.model.Waypoint
import com.google.android.libraries.places.api.model.Place
import org.json.JSONObject

class Util {

    companion object {
        fun placeToWaypoint(place: Place): Waypoint {
            val json = JSONObject()
            json.put("name", place.name)
            json.put("point", "${place.latLng!!.latitude},${place.latLng!!.longitude}")
            json.put("url", place.iconUrl)
            json.put("color", place.iconBackgroundColor)
            return Waypoint(json)
        }
    }
}
package com.apm.jacx.model

import android.util.Log
import org.json.JSONObject

class Waypoint(json: JSONObject) {

    var id: Int? = null
    var name: String? = null
    var orderPosition: Int? = null
    var point: String? = null
    var url: String? = null
    var color: Int? = null

    init {
        if (json.has("id") && !json.isNull("id")) {
            id = json.get("id") as Int
        }
        if (json.has("name") && !json.isNull("name")) {
            name = json.get("name") as String
        }
        if (json.has("orderPosition") && !json.isNull("orderPosition")) {
            orderPosition = json.get("orderPosition") as Int
        }
        if (json.has("point") && !json.isNull("point")) {
            point = json.get("point") as String
        }
        if (json.has("url") && !json.isNull("url")) {
            url = json.get("url") as String
        }
        if (json.has("color") && !json.isNull("color")) {
            color = json.get("color") as Int
        }
    }

    fun toJSONObject(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("name", name)
        json.put("orderPosition", orderPosition)
        json.put("point", point)
        json.put("url", url)
        json.put("color", color)
        return json
    }
}
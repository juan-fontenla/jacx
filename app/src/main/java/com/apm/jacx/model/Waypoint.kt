package com.apm.jacx.model

import org.json.JSONObject

class Waypoint(json: JSONObject) {

    var id: Int? = null
    var name: String? = null
    var orderPosition: Int? = null
    var point: String? = null
    var url: String? = null
    var color: Int? = null

    init {
        if (json.has("id")) {
            id = json.get("id") as Int
        }
        if (json.has("name")) {
            name = json.get("name") as String
        }
        if (json.has("orderPosition")) {
            orderPosition = json.get("orderPosition") as Int
        }
        if (json.has("point")) {
            point = json.get("point") as String
        }
        if (json.has("url")) {
            url = json.get("url") as String
        }
        if (json.has("color")) {
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
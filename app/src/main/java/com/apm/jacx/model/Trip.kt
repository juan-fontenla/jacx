package com.apm.jacx.model

import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class Trip(json: JSONObject) {

    var id: Int
    var name: String
    var begin: Waypoint
    var waypoints: MutableList<Waypoint> = mutableListOf()
    var finish: Waypoint
    var startDate: LocalDate
    var endDate: LocalDate

    init {
        id = json.get("id") as Int
        name = json.get("name") as String
        begin = Waypoint(json.get("begin") as JSONObject)
        if (json.has("wayPoints") && !json.isNull("wayPoints")) {
            val waypointArray = json.get("wayPoints") as JSONArray
            for (i in 0 until waypointArray.length()) {
                waypoints.add(Waypoint(waypointArray[i] as JSONObject))
            }
        }
        finish = Waypoint(json.get("finish") as JSONObject)
        startDate = LocalDate.parse(json.get("startDate") as String)
        endDate = LocalDate.parse(json.get("endDate") as String)
    }
}
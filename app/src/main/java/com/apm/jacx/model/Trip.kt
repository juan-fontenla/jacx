package com.apm.jacx.model

import org.json.JSONObject
import java.time.LocalDate

class Trip(json: JSONObject) {

    var id: Int
    var name: String
    var begin: Waypoint
    var finish: Waypoint
    var startDate: LocalDate
    var endDate: LocalDate

    init {
        id = json.get("id") as Int
        name = json.get("name") as String
        begin = Waypoint(json.get("begin") as JSONObject)
        finish = Waypoint(json.get("finish") as JSONObject)
        startDate = LocalDate.parse(json.get("startDate") as String)
        endDate = LocalDate.parse(json.get("endDate") as String)
    }
}
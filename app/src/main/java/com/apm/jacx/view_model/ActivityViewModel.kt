package com.apm.jacx.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apm.jacx.client.ApiClient
import com.apm.jacx.model.Trip
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ActivityViewModel: ViewModel() {
    private val _apiResult = MutableLiveData<Trip>()
    val apiResult: LiveData<Trip> get() = _apiResult

    fun makeApiRequest(id: Int) = runBlocking {
        // Realiza la petición a la API y obtén el resultado
        val result = JSONObject(ApiClient.get("/route/id/${id}"))
        _apiResult.value = Trip(result)
    }
}
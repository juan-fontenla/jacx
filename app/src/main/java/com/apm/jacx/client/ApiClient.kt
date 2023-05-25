package com.apm.jacx.client

import android.util.Log
import com.apm.jacx.internalStorage.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ApiClient {
    private const val BASE_URL = "jacx-back-production.up.railway.app"
    private const val SCHEME = "https"

    private val client = OkHttpClient.Builder().build()

    private fun setupHeaders(): Headers {
        val headers = Headers.Builder()
        if(AppPreferences.TOKEN_BD != null) {
            headers.add("Authorization", AppPreferences.TOKEN_BD!!)
        }
        return headers.build()
    }

    suspend fun get(path: String): String = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme(SCHEME).host(BASE_URL).addPathSegments("api$path").build()
        val request = Request.Builder().url(url).headers(setupHeaders()).get().build()

        executeRequest(request)
    }

    suspend fun post(path: String, jsonBody: String): String = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme(SCHEME).host(BASE_URL).addPathSegments("api$path").build()
        val request = Request.Builder().url(url).headers(setupHeaders())
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        executeRequest(request)
    }

    suspend fun put(path: String, jsonBody: String): String = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme(SCHEME).host(BASE_URL).addPathSegments("api$path").build()
        val request = Request.Builder().url(url).headers(setupHeaders())
            .put(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        executeRequest(request)
    }

    suspend fun delete(path: String, jsonBody: String): String = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme("https").host(BASE_URL).addPathSegments("api$path").build()
        val request = Request.Builder().url(url).headers(setupHeaders())
            .delete(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        executeRequest(request)
    }

    private fun executeRequest(request: Request): String {
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: ""

        if (response.isSuccessful) {
            return body
        } else {
            throw IOException("Request failed with code: ${response.code}, body: $body")
        }
    }
}
package com.apm.jacx.spotify.interceptor

import com.apm.jacx.internalStorage.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    private val token = AppPreferences.TOKEN_SPOTIFY;

  /*  var prefs = PreferenceManager.getDefaultSharedPreferences(Context.);
    prefs = prefs.getSharedPreferences(Resources.getSystem().getString(),
        Context.MODE_PRIVATE
    );*/
    // Para cada peticion se añade en el header de cada peticion
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )
}
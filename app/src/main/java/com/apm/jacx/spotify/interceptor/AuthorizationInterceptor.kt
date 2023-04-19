package com.apm.jacx.spotify.interceptor
import com.apm.jacx.MainApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    // Para cada peticion se añade en el header de cada peticion
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${MainApplication.TOKEN}")
                .build()
        )
}
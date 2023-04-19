package com.apm.jacx

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class MainApplication : Application() {

    companion object {
        var TOKEN: String? = null
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
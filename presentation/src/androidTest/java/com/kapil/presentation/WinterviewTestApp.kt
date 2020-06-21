package com.kapil.presentation

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class WinterviewTestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))
        startKoin {
            androidContext(applicationContext)
            modules(emptyList())
        }
    }
}
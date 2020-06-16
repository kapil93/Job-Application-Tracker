package com.kapil.winterview

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kapil.data.common.JobsRealmModule
import com.kapil.winterview.dimodule.deviceModule
import com.kapil.winterview.dimodule.eventsModule
import com.kapil.winterview.dimodule.jobsModule
import com.kapil.winterview.dimodule.offersModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class WinterviewApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Realm.init(applicationContext)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .addModule(JobsRealmModule())
                .build()
        )

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    jobsModule,
                    eventsModule,
                    offersModule,
                    deviceModule
                )
            )
        }
    }
}
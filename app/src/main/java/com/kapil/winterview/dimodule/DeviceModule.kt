package com.kapil.winterview.dimodule

import com.kapil.device.ExternalAppLauncherImpl
import com.kapil.domain.openapp.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val deviceModule = module {

    single { OpenBrowserUseCase(get()) }
    single { OpenMapsUseCase(get()) }
    single { OpenEmailClientUseCase(get()) }
    single { OpenDialerUseCase(get()) }
    single { OpenCalendarUseCase(get()) }

    single<ExternalAppLauncher> { ExternalAppLauncherImpl(androidContext()) }
}
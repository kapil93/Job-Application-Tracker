package com.kapil.domain.openapp

import com.kapil.domain.entity.Event
import io.reactivex.rxjava3.core.Completable

interface ExternalAppLauncher {
    fun openEmailClient(emailId: String): Completable
    fun openDialer(phoneNumber: String): Completable
    fun openMaps(address: String): Completable
    fun openBrowser(url: String): Completable
    fun openCalendar(event: Event): Completable
}
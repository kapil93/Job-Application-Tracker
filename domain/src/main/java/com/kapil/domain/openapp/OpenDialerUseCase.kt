package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenDialerUseCase(private val externalAppLauncher: ExternalAppLauncher) {
    fun execute(phoneNumber: String): Completable = externalAppLauncher.openDialer(phoneNumber)
}
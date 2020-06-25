package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenMapsUseCase(private val externalAppLauncher: ExternalAppLauncher) {
    fun execute(address: String): Completable = externalAppLauncher.openMaps(address)
}
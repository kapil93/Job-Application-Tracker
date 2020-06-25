package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenEmailClientUseCase(private val externalAppLauncher: ExternalAppLauncher) {
    fun execute(emailId: String): Completable = externalAppLauncher.openEmailClient(emailId)
}
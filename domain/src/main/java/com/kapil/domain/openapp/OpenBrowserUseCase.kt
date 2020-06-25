package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenBrowserUseCase(private val externalAppLauncher: ExternalAppLauncher) {
    fun execute(url: String): Completable = externalAppLauncher.openBrowser(url)
}
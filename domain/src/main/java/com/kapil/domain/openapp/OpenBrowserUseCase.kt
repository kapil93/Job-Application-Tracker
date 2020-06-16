package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenBrowserUseCase(private val openAppRepository: OpenAppRepository) {
    fun execute(url: String): Completable = openAppRepository.openBrowser(url)
}
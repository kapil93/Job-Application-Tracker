package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenMapsUseCase(private val openAppRepository: OpenAppRepository) {
    fun execute(address: String): Completable = openAppRepository.openMaps(address)
}
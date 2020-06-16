package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenDialerUseCase(private val openAppRepository: OpenAppRepository) {
    fun execute(phoneNumber: String): Completable = openAppRepository.openDialer(phoneNumber)
}
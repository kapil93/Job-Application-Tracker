package com.kapil.domain.openapp

import io.reactivex.rxjava3.core.Completable

class OpenEmailClientUseCase(private val openAppRepository: OpenAppRepository) {
    fun execute(emailId: String): Completable = openAppRepository.openEmailClient(emailId)
}
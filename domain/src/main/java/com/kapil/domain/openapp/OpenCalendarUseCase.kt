package com.kapil.domain.openapp

import com.kapil.domain.entity.Event
import io.reactivex.rxjava3.core.Completable

class OpenCalendarUseCase(private val openAppRepository: OpenAppRepository) {
    fun execute(event: Event): Completable = openAppRepository.openCalendar(event)
}
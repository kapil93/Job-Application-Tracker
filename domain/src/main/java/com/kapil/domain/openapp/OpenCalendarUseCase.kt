package com.kapil.domain.openapp

import com.kapil.domain.entity.Event
import io.reactivex.rxjava3.core.Completable

class OpenCalendarUseCase(private val externalAppLauncher: ExternalAppLauncher) {
    fun execute(event: Event): Completable = externalAppLauncher.openCalendar(event)
}
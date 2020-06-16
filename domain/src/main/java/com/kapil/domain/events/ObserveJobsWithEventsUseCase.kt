package com.kapil.domain.events

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.core.Flowable

class ObserveJobsWithEventsUseCase(private val eventsRepository: EventsRepository) {
    fun execute(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        eventsRepository.observeJobsWithEvents(jobFilterProperties)
}
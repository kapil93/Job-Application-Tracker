package com.kapil.data.events

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.domain.events.EventsRepository
import io.reactivex.rxjava3.core.Flowable

class EventsRepositoryImpl(private val dbDataSource: EventsDbDataSource) : EventsRepository {

    override fun observeJobsWithEvents(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        dbDataSource.observeJobsWithEvents(jobFilterProperties)
}
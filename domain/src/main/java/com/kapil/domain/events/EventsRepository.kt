package com.kapil.domain.events

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.core.Flowable

interface EventsRepository {
    fun observeJobsWithEvents(jobFilterProperties: JobFilterProperties): Flowable<List<Job>>
}
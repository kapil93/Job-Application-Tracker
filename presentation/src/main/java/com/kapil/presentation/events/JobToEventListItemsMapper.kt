package com.kapil.presentation.events

import com.kapil.domain.entity.Event
import com.kapil.domain.entity.Job
import com.kapil.presentation.common.formatDateTime
import io.reactivex.rxjava3.functions.Function

class JobToEventListItemsMapper : Function<Job, List<EventListItem>> {

    override fun apply(job: Job) = job.events.map { event ->
        EventListItem(
            jobId = job.id,
            title = event.title,
            companyName = job.company.name,
            role = job.role,
            startTime = event.startTime.formatDateTime(),
            endTime = (event as? Event)?.endTime?.formatDateTime() ?: "",
            companyAddress = job.company.address ?: "",
            notes = event.notes ?: ""
        )
    }
}
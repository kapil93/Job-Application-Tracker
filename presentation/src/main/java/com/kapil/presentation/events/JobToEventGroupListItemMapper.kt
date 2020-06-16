package com.kapil.presentation.events

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.functions.Function

class JobToEventGroupListItemMapper : Function<Job, EventGroupListItem> {

    override fun apply(job: Job) = EventGroupListItem(
        jobId = job.id,
        companyName = job.company.name,
        role = job.role,
        companyAddress = job.company.address ?: "",
        events = job.events
    )
}
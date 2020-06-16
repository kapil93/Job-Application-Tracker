package com.kapil.presentation.jobs

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.functions.Function


class JobToJobListItemMapper : Function<Job, JobListItem> {

    override fun apply(job: Job) = JobListItem(
        id = job.id,
        role = job.role,
        companyName = job.company.name,
        priority = job.priority,
        contactName = job.contact?.name ?: "",
        address = job.company.address ?: "",
        status = job.status
    )
}
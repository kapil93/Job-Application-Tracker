package com.kapil.presentation.jobs.addoredit

import com.kapil.domain.entity.Job
import com.kapil.presentation.common.formatDate
import io.reactivex.rxjava3.functions.Function

class JobToJobFormMapper : Function<Job, JobForm> {

    override fun apply(job: Job): JobForm = JobForm(
        id = job.id,
        role = job.role,
        companyName = job.company.name,
        companyWebsite = job.company.website ?: "",
        companyAddress = job.company.address ?: "",
        companyNotes = job.company.notes ?: "",
        contactName = job.contact?.name ?: "",
        contactEmail = job.contact?.email ?: "",
        contactPhone = job.contact?.phone ?: "",
        priority = job.priority,
        status = job.status,
        offerAmount = job.offer?.amount ?: "",
        offerDateOfJoining = job.offer?.dateOfJoining?.formatDate() ?: "",
        offerNotes = job.offer?.notes ?: "",
        newEventTitle = "",
        newEventStartTime = "",
        newEventEndTime = "",
        newEventNotes = "",
        events = job.events,
        appliedThrough = job.appliedThrough ?: "",
        notes = job.notes ?: ""
    )
}
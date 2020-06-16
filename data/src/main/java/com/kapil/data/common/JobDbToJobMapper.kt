package com.kapil.data.common

import com.kapil.data.common.realmmodels.*
import com.kapil.domain.entity.*
import io.reactivex.rxjava3.functions.Function

class JobDbToJobMapper : Function<JobDb, Job> {

    override fun apply(jobDb: JobDb) = Job(
        id = jobDb.id.takeUnless { it == JobDb.ID_ILLEGAL_VALUE } ?: Job.ID_ILLEGAL_VALUE,
        role = jobDb.role,
        company = jobDb.company!!.transformToCompany(),
        priority = jobDb.priority.transformToPriority(),
        contact = jobDb.contact?.transformToContact(),
        status = jobDb.status.transformToStatus(),
        offer = jobDb.offer?.transformToOffer(),
        events = jobDb.events.map(::transformToEvent),
        appliedThrough = jobDb.appliedThrough,
        notes = jobDb.notes
    )

    private fun CompanyDb.transformToCompany() = Company(
        name = name,
        website = website,
        address = address,
        notes = notes
    )

    private fun ContactDb.transformToContact() = Contact(
        name = name,
        email = email,
        phone = phone
    )

    private fun OfferDb.transformToOffer() = Offer(
        amount = amount,
        dateOfJoining = dateOfJoining,
        notes = notes
    )

    private fun transformToEvent(eventDb: EventDb) = Event(
        title = eventDb.title,
        startTime = eventDb.startTime,
        endTime = eventDb.endTime,
        notes = eventDb.notes
    )

    private fun JobDb.PriorityDb.transformToPriority() = when (this) {
        JobDb.PriorityDb.LOW -> Job.Priority.LOW
        JobDb.PriorityDb.MEDIUM -> Job.Priority.MEDIUM
        JobDb.PriorityDb.HIGH -> Job.Priority.HIGH
        JobDb.PriorityDb.VERY_HIGH -> Job.Priority.VERY_HIGH
    }

    private fun JobDb.StatusDb.transformToStatus() = when (this) {
        JobDb.StatusDb.TO_APPLY -> Job.Status.TO_APPLY
        JobDb.StatusDb.APPLIED -> Job.Status.APPLIED
        JobDb.StatusDb.IN_PROCESS -> Job.Status.IN_PROCESS
        JobDb.StatusDb.DID_NOT_WORK_OUT -> Job.Status.DID_NOT_WORK_OUT
        JobDb.StatusDb.GOT_THE_JOB -> Job.Status.GOT_THE_JOB
    }
}
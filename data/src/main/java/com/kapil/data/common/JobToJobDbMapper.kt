package com.kapil.data.common

import com.kapil.data.common.realmmodels.*
import com.kapil.domain.entity.*
import io.reactivex.rxjava3.functions.Function

class JobToJobDbMapper : Function<Job, JobDb> {

    override fun apply(job: Job) = JobDb(
        id = job.id.takeUnless { it == Job.ID_ILLEGAL_VALUE } ?: JobDb.ID_ILLEGAL_VALUE,
        role = job.role,
        company = job.company.transformToCompanyDb(),
        contact = job.contact?.transformToContactDb(),
        offer = job.offer?.transformToOfferDb(),
        events = job.events.map(::transformToEventDb).toRealmList(),
        appliedThrough = job.appliedThrough,
        notes = job.notes
    ).apply {
        priority = job.priority.transformToPriorityDb()
        status = job.status.transformToStatusDb()
    }

    private fun Company.transformToCompanyDb() =
        CompanyDb(
            name = name,
            website = website,
            address = address,
            notes = notes
        )

    private fun Contact.transformToContactDb() =
        ContactDb(
            name = name,
            email = email,
            phone = phone
        )

    private fun Offer.transformToOfferDb() =
        OfferDb(
            amount = amount,
            dateOfJoining = dateOfJoining,
            notes = notes
        )

    private fun transformToEventDb(event: Event) = EventDb(
        title = event.title,
        startTime = event.startTime,
        endTime = event.endTime,
        notes = event.notes
    )

    private fun Job.Priority.transformToPriorityDb() = when (this) {
        Job.Priority.LOW -> JobDb.PriorityDb.LOW
        Job.Priority.MEDIUM -> JobDb.PriorityDb.MEDIUM
        Job.Priority.HIGH -> JobDb.PriorityDb.HIGH
        Job.Priority.VERY_HIGH -> JobDb.PriorityDb.VERY_HIGH
    }

    private fun Job.Status.transformToStatusDb() = when (this) {
        Job.Status.TO_APPLY -> JobDb.StatusDb.TO_APPLY
        Job.Status.APPLIED -> JobDb.StatusDb.APPLIED
        Job.Status.IN_PROCESS -> JobDb.StatusDb.IN_PROCESS
        Job.Status.DID_NOT_WORK_OUT -> JobDb.StatusDb.DID_NOT_WORK_OUT
        Job.Status.GOT_THE_JOB -> JobDb.StatusDb.GOT_THE_JOB
    }
}
package com.kapil.presentation.offers

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.functions.Function


class JobToOfferListItemMapper : Function<Job, OfferListItem> {

    override fun apply(job: Job) = OfferListItem(
        jobId = job.id,
        role = job.role,
        companyName = job.company.name,
        dateOfJoining = job.offer!!.dateOfJoining,
        amount = job.offer!!.amount,
        notes = job.offer!!.notes ?: ""
    )
}
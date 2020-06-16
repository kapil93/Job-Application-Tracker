package com.kapil.domain.offers

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Flowable

interface OffersRepository {
    fun observeJobsWithOffer(): Flowable<List<Job>>
}
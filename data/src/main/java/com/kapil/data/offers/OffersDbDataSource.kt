package com.kapil.data.offers

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Flowable

interface OffersDbDataSource {
    fun observeJobsWithOffer(): Flowable<List<Job>>
}
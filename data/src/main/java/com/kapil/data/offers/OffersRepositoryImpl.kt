package com.kapil.data.offers

import com.kapil.domain.entity.Job
import com.kapil.domain.offers.OffersRepository
import io.reactivex.rxjava3.core.Flowable

class OffersRepositoryImpl(private val dbDataSource: OffersDbDataSource) : OffersRepository {

    override fun observeJobsWithOffer(): Flowable<List<Job>> = dbDataSource.observeJobsWithOffer()
}
package com.kapil.domain.offers

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Flowable

class ObserveJobsWithOfferUseCase(private val offersRepository: OffersRepository) {
    fun execute(): Flowable<List<Job>> = offersRepository.observeJobsWithOffer()
}

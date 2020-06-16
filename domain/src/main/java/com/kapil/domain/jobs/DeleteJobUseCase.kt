package com.kapil.domain.jobs

import io.reactivex.rxjava3.core.Completable

class DeleteJobUseCase(private val jobsRepository: JobsRepository) {
    fun execute(jobId: Long): Completable = jobsRepository.deleteJob(jobId)
}

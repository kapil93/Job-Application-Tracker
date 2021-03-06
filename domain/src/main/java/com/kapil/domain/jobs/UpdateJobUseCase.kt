package com.kapil.domain.jobs

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Completable

class UpdateJobUseCase(private val jobsRepository: JobsRepository) {
    fun execute(job: Job): Completable = jobsRepository.updateJob(job)
}
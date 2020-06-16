package com.kapil.domain.jobs

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Single

class LoadJobUseCase(private val jobsRepository: JobsRepository) {
    fun execute(jobId: Long): Single<Job> = jobsRepository.loadJob(jobId)
}

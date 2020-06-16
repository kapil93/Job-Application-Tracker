package com.kapil.domain.jobs

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface JobsRepository {
    fun observeJobList(jobFilterProperties: JobFilterProperties): Flowable<List<Job>>
    fun loadJob(jobId: Long): Single<Job>
    fun createJob(job: Job): Completable
    fun updateJob(job: Job): Completable
    fun deleteJob(jobId: Long): Completable
}
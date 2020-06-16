package com.kapil.data.jobs

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface JobsDbDataSource {
    fun observeAllJobs(jobFilterProperties: JobFilterProperties): Flowable<List<Job>>
    fun loadJob(jobId: Long): Single<Job>
    fun saveOrUpdateJob(job: Job): Completable
    fun deleteJob(jobId: Long): Completable
    fun clearAll(): Completable
}
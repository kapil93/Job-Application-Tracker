package com.kapil.data.jobs

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.domain.jobs.JobsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class JobsRepositoryImpl(private val dbDataSource: JobsDbDataSource) : JobsRepository {

    override fun observeJobList(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        dbDataSource.observeAllJobs(jobFilterProperties)

    override fun loadJob(jobId: Long): Single<Job> = dbDataSource.loadJob(jobId)

    override fun createJob(job: Job): Completable = dbDataSource.saveOrUpdateJob(job)

    override fun updateJob(job: Job): Completable = dbDataSource.saveOrUpdateJob(job)

    override fun deleteJob(jobId: Long): Completable = dbDataSource.deleteJob(jobId)
}
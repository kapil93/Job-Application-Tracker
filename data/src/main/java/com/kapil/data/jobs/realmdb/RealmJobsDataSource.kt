package com.kapil.data.jobs.realmdb

import com.kapil.data.common.*
import com.kapil.data.common.realmmodels.JobDb
import com.kapil.data.jobs.JobsDbDataSource
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmJobsDataSource(
    private val jobDbToJobMapper: JobDbToJobMapper,
    private val jobToJobDbMapper: JobToJobDbMapper
) : JobsDbDataSource {

    override fun observeAllJobs(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        openRealmInstance().flatMapPublisher { realm ->
            RxJavaBridge.toV3Flowable<MutableList<JobDb>>(
                realm.where<JobDb>()
                    .applyJobFilterQueries(jobFilterProperties)
                    .findAll()
                    .asFlowable()
                    .onBackpressureLatest()
                    .filter(RealmResults<JobDb>::isLoaded)
                    .map { realm.copyFromRealm(it) }
            )
        }.applyRealmSchedulers()
            .flatMapSingle { list ->
                Observable.fromIterable(list)
                    .map(jobDbToJobMapper)
                    .toList()
            }

    override fun loadJob(jobId: Long): Single<Job> = openRealmInstance().map {
        val job = it.where<JobDb>().equalTo(JobDb.FIELDS.ID, jobId).findFirst()
            ?: throw RuntimeException("job id: $jobId not found")
        it.copyFromRealm(job)
    }.map(jobDbToJobMapper)

    override fun saveOrUpdateJob(job: Job): Completable = openRealmInstance().map { realm ->
        realm.executeTransaction {
            val jobDb = jobToJobDbMapper.apply(job)
            // First, deep delete the job to avoid duplication of related entities with no primary
            // key.
            it.where<JobDb>().equalTo(JobDb.FIELDS.ID, jobDb.id).findFirst()?.deepDeleteJob()
            it.insert(jobDb.assignId(it))
        }
    }.ignoreElement()

    override fun deleteJob(jobId: Long): Completable = openRealmInstance().map { realm ->
        realm.executeTransaction {
            (it.where<JobDb>().equalTo(JobDb.FIELDS.ID, jobId).findFirst()
                ?: throw RuntimeException("job id: $jobId not found")).deepDeleteJob()
        }
    }.ignoreElement()

    override fun clearAll(): Completable = openRealmInstance().map { realm ->
        realm.executeTransaction {
            it.deleteAll()
        }
    }.ignoreElement()
}
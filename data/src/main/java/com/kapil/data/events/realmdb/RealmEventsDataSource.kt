package com.kapil.data.events.realmdb

import com.kapil.data.common.JobDbToJobMapper
import com.kapil.data.common.applyJobFilterQueries
import com.kapil.data.common.applyRealmSchedulers
import com.kapil.data.common.openRealmInstance
import com.kapil.data.common.realmmodels.JobDb
import com.kapil.data.events.EventsDbDataSource
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmEventsDataSource(private val jobDbToJobMapper: JobDbToJobMapper) : EventsDbDataSource {

    override fun observeJobsWithEvents(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        openRealmInstance().flatMapPublisher { realm ->
            RxJavaBridge.toV3Flowable<MutableList<JobDb>>(
                realm.where<JobDb>()
                    .applyJobFilterQueries(jobFilterProperties)
                    .isNotEmpty(JobDb.FIELDS.EVENTS)
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
}
package com.kapil.data.offers.realmdb

import com.kapil.data.common.JobDbToJobMapper
import com.kapil.data.common.applyRealmSchedulers
import com.kapil.data.common.openRealmInstance
import com.kapil.data.common.realmmodels.JobDb
import com.kapil.data.offers.OffersDbDataSource
import com.kapil.domain.entity.Job
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmOffersDataSource(private val jobDbToJobMapper: JobDbToJobMapper) : OffersDbDataSource {

    override fun observeJobsWithOffer(): Flowable<List<Job>> =
        openRealmInstance().flatMapPublisher { realm ->
            RxJavaBridge.toV3Flowable<MutableList<JobDb>>(
                realm.where<JobDb>().isNotNull(JobDb.FIELDS.OFFER).findAll().asFlowable()
                    .onBackpressureLatest()
                    .filter(RealmResults<JobDb>::isLoaded)
                    .map { realm.copyFromRealm(it) }
            )
        }.applyRealmSchedulers()
            .flatMapSingle {
                Observable.fromIterable(it)
                    .map(jobDbToJobMapper)
                    .toList()
            }
}
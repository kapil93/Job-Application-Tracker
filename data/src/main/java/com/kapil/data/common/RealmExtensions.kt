package com.kapil.data.common

import android.os.HandlerThread
import com.kapil.data.common.realmmodels.CompanyDb
import com.kapil.data.common.realmmodels.JobDb
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmQuery
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.oneOf
import timber.log.Timber


private val realmScheduler = createRealmScheduler()

fun openRealmInstance(): Single<Realm> = Single.create {
    var realm: Realm? = null
    val closeRealm = {
        Timber.d("Realm instance closed, already closed: ${realm == null}")
        realm?.close()
        realm = null
    }
    // To be safe, close realm both when the observable is cancelled or disposed.
    it.setDisposable(Disposable.fromRunnable(closeRealm))
    it.setCancellable(closeRealm)
    try {
        realm = Realm.getDefaultInstance()
        Timber.d("Realm instance created")
        it.onSuccess(realm!!)
    } catch (e: NullPointerException) {
        it.onError(e)
    } catch (e: RealmMigrationNeededException) {
        it.onError(e)
    } catch (e: RealmFileException) {
        it.onError(e)
    }
}

inline fun <reified T> List<T>.toRealmList(): RealmList<T> = RealmList(*toTypedArray())

fun <T : Enum<T>> Set<T>.toStringArray(): Array<String> = map { it.name }.toTypedArray()

private fun createRealmScheduler(): Scheduler {
    val handlerThread = HandlerThread("realm-worker")
    if (!handlerThread.isAlive)
        handlerThread.start()
    if (handlerThread.looper != null) {
        return AndroidSchedulers.from(handlerThread.looper)
    }
    return Schedulers.io()
}

fun <T> Flowable<T>.applyRealmSchedulers(): Flowable<T> = compose {
    it.subscribeOn(realmScheduler)
        .observeOn(realmScheduler)
        .unsubscribeOn(realmScheduler)
}

fun JobDb.deepDeleteJob() {
    company?.deleteFromRealm()
    contact?.deleteFromRealm()
    offer?.deleteFromRealm()
    events.deleteAllFromRealm()
    deleteFromRealm()
}

fun RealmQuery<JobDb>.applyJobFilterQueries(jobFilterProperties: JobFilterProperties) = apply {
    jobFilterProperties.companyKeyword?.let {
        val fieldName = "${JobDb.FIELDS.COMPANY}.${CompanyDb.FIELDS.NAME}"
        beginsWith(fieldName, it, Case.INSENSITIVE)
    }
    jobFilterProperties.roleKeyword?.let {
        beginsWith(JobDb.FIELDS.ROLE, it, Case.INSENSITIVE)
    }
    oneOf(JobDb.FIELDS.STATUS, jobFilterProperties.statusSet.toStringArray())
    oneOf(JobDb.FIELDS.PRIORITY, jobFilterProperties.prioritySet.toStringArray())
}
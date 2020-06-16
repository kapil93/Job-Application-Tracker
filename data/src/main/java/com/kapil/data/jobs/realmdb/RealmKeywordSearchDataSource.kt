package com.kapil.data.jobs.realmdb

import com.kapil.data.common.openRealmInstance
import com.kapil.data.common.realmmodels.CompanyDb
import com.kapil.data.common.realmmodels.JobDb
import com.kapil.data.jobs.KeywordSearchDbDataSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.realm.Case
import io.realm.kotlin.where

class RealmKeywordSearchDataSource(
    // Declared as a constructor parameter as the option to set this value dynamically is not
    // intended to be supported yet.
    private val keywordSearchResultCountLimit: Int
) : KeywordSearchDbDataSource {

    override fun loadRoleSearchResults(roleKeyword: String): Single<List<String>> =
        openRealmInstance().map { realm ->
            realm.where<JobDb>()
                .beginsWith(JobDb.FIELDS.ROLE, roleKeyword, Case.INSENSITIVE)
                .distinct(JobDb.FIELDS.ROLE)
                .limit(keywordSearchResultCountLimit.toLong())
                .sort(JobDb.FIELDS.ROLE)
                .findAll()
                .map { realm.copyFromRealm(it) }
        }.flatMapObservable { Observable.fromIterable(it) }
            .map { jobDb -> jobDb.role }
            .toList()

    override fun loadCompanySearchResults(companyKeyword: String): Single<List<String>> =
        openRealmInstance().map { realm ->
            realm.where<CompanyDb>()
                .beginsWith(CompanyDb.FIELDS.NAME, companyKeyword, Case.INSENSITIVE)
                .distinct(CompanyDb.FIELDS.NAME)
                .limit(keywordSearchResultCountLimit.toLong())
                .sort(CompanyDb.FIELDS.NAME)
                .findAll()
                .map { realm.copyFromRealm(it) }
        }.flatMapObservable { Observable.fromIterable(it) }
            .map { companyDb -> companyDb.name }
            .toList()
}
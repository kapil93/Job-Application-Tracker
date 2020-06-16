package com.kapil.data.jobs

import io.reactivex.rxjava3.core.Single

interface KeywordSearchDbDataSource {
    fun loadRoleSearchResults(roleKeyword: String): Single<List<String>>
    fun loadCompanySearchResults(companyKeyword: String): Single<List<String>>
}
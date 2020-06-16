package com.kapil.domain.jobs.keywordsearch

import io.reactivex.rxjava3.core.Single

interface KeywordSearchRepository {
    fun loadRoleSearchResults(roleKeyword: String): Single<List<String>>
    fun loadCompanySearchResults(companyKeyword: String): Single<List<String>>
}
package com.kapil.data.jobs

import com.kapil.domain.jobs.keywordsearch.KeywordSearchRepository
import io.reactivex.rxjava3.core.Single

class KeywordSearchRepositoryImpl(
    private val dbDataSource: KeywordSearchDbDataSource
) : KeywordSearchRepository {

    override fun loadRoleSearchResults(roleKeyword: String): Single<List<String>> =
        dbDataSource.loadRoleSearchResults(roleKeyword)

    override fun loadCompanySearchResults(companyKeyword: String): Single<List<String>> =
        dbDataSource.loadCompanySearchResults(companyKeyword)
}
package com.kapil.domain.jobs.keywordsearch

import io.reactivex.rxjava3.core.Single

class LoadCompanySearchResultsUseCase(private val keywordSearchRepository: KeywordSearchRepository) {

    fun execute(companyKeyword: String): Single<List<String>> =
        keywordSearchRepository.loadCompanySearchResults(companyKeyword)
}

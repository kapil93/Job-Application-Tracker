package com.kapil.domain.jobs.keywordsearch

import io.reactivex.rxjava3.core.Single

class LoadRoleSearchResultsUseCase(private val keywordSearchRepository: KeywordSearchRepository) {

    fun execute(roleKeyword: String): Single<List<String>> =
        keywordSearchRepository.loadRoleSearchResults(roleKeyword)
}

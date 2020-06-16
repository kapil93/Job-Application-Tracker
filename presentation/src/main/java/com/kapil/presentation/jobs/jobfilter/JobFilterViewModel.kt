package com.kapil.presentation.jobs.jobfilter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.jobs.keywordsearch.LoadCompanySearchResultsUseCase
import com.kapil.domain.jobs.keywordsearch.LoadRoleSearchResultsUseCase
import com.kapil.presentation.base.BaseViewModel
import com.kapil.presentation.common.ShortLivedItem
import timber.log.Timber

class JobFilterViewModel(
    private val loadRoleSearchResultsUseCase: LoadRoleSearchResultsUseCase,
    private val loadCompanySearchResultsUseCase: LoadCompanySearchResultsUseCase
) : BaseViewModel() {

    private val _roleSearchResults = MutableLiveData<ShortLivedItem<List<String>>>()
    private val _companySearchResults = MutableLiveData<ShortLivedItem<List<String>>>()

    val roleSearchResults: LiveData<ShortLivedItem<List<String>>> = _roleSearchResults
    val companySearchResults: LiveData<ShortLivedItem<List<String>>> = _companySearchResults

    fun loadRoleFilterKeywordResults(roleKeyword: String) {
        disposable.add(
            loadRoleSearchResultsUseCase.execute(roleKeyword)
                .applySchedulers()
                .subscribe({
                    _roleSearchResults.value = ShortLivedItem(it)
                    Timber.d("New role search results successfully loaded")
                }, {
                    Timber.e(it, "Failed to load role search results")
                })
        )
    }

    fun loadCompanyFilterKeywordResults(companyKeyword: String) {
        disposable.add(
            loadCompanySearchResultsUseCase.execute(companyKeyword)
                .applySchedulers()
                .subscribe({
                    _companySearchResults.value = ShortLivedItem(it)
                    Timber.d("New company search results successfully loaded")
                }, {
                    Timber.e(it, "Failed to load company search results")
                })
        )
    }
}

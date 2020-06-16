package com.kapil.presentation.offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.entity.Job
import com.kapil.domain.offers.ObserveJobsWithOfferUseCase
import com.kapil.presentation.base.BaseViewModel
import com.kapil.presentation.common.hasValue
import timber.log.Timber

class OffersViewModel(
    private val observeJobsWithOfferUseCase: ObserveJobsWithOfferUseCase,
    private val mapper: JobToOfferListItemMapper
) : BaseViewModel() {

    private val _offerListViewEntity = MutableLiveData<OfferListViewEntity>()

    val offerListViewEntity: LiveData<OfferListViewEntity> = _offerListViewEntity

    fun observeOfferList() {
        if (offerListViewEntity.hasValue()) {
            Timber.d("Already observing offer list")
            return
        }
        disposable.add(
            observeJobsWithOfferUseCase.execute()
                .map(::mapToOfferListViewEntity)
                .applySchedulers()
                .applyLoadingObservable()
                .applyErrorObservable(::observeOfferList)
                .doOnSubscribe { Timber.d("Observing offer list") }
                .subscribe({
                    _offerListViewEntity.value = it
                    Timber.d("New offer list successfully loaded")
                }, {
                    Timber.e(it, "Failed to observe offer list")
                }, {
                    Timber.d("No longer observing offer list")
                })
        )
    }

    fun onSortOrderChanged(sortOrder: SortOrder) {
        if (offerListViewEntity.hasValue()) {
            _offerListViewEntity.value = OfferListViewEntity(
                sortOrder = sortOrder,
                offerList = offerListViewEntity.value!!.offerList.sortOfferList(sortOrder)
            )
        } else {
            Timber.d("OfferListViewEntity is empty")
        }
    }

    private fun mapToOfferListViewEntity(jobList: List<Job>): OfferListViewEntity {
        val currentSortOrder = offerListViewEntity.value?.sortOrder ?: DEFAULT_SORT_ORDER
        return OfferListViewEntity(
            sortOrder = currentSortOrder,
            offerList = jobList.map(mapper::apply).sortOfferList(currentSortOrder)
        )
    }
}

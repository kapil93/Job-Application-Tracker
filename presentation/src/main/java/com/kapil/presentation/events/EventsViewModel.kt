package com.kapil.presentation.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.entity.Event
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.domain.events.ObserveJobsWithEventsUseCase
import com.kapil.domain.openapp.OpenCalendarUseCase
import com.kapil.presentation.base.BaseViewModel
import com.kapil.presentation.common.ShortLivedItem
import com.kapil.presentation.common.hasValue
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber

class EventsViewModel(
    private val observeJobsWithEventsUseCase: ObserveJobsWithEventsUseCase,
    private val openCalendarUseCase: OpenCalendarUseCase,
    private val jobToEventGroupListItemMapper: JobToEventGroupListItemMapper,
    private val jobToEventListItemsMapper: JobToEventListItemsMapper,
    private val eventListItemToEventMapper: EventListItemToEventMapper
) : BaseViewModel() {

    private val _eventListViewEntity = MutableLiveData<EventListViewEntity>()
    private val _isOpenCalendarRequestSuccessful = MutableLiveData<ShortLivedItem<Boolean>>()

    val eventListViewEntity: LiveData<EventListViewEntity> = _eventListViewEntity
    val isOpenCalendarRequestSuccessful: LiveData<ShortLivedItem<Boolean>> =
        _isOpenCalendarRequestSuccessful

    private lateinit var onFilterPropertiesChangedListener: (JobFilterProperties) -> Unit

    private val filterPropertiesObservable = Flowable.create<JobFilterProperties>({ emitter ->
        if (!emitter.isCancelled) emitter.onNext(JobFilterProperties.DEFAULT)
        onFilterPropertiesChangedListener = { if (!emitter.isCancelled) emitter.onNext(it) }
    }, BackpressureStrategy.LATEST).subscribeOn(AndroidSchedulers.mainThread())

    fun observeEventList() {
        if (eventListViewEntity.hasValue()) {
            Timber.d("Already observing event list")
            return
        }
        disposable.add(
            filterPropertiesObservable
                .distinctUntilChanged()
                .applySchedulersAndLoadingObservableTo {
                    switchMap { jobFilterProperties ->
                        observeJobsWithEventsUseCase.execute(jobFilterProperties)
                            .map { mapToEventListViewEntity(it, jobFilterProperties) }
                    }
                }
                .applyErrorObservable(::observeEventList)
                .doOnSubscribe { Timber.d("Observing event list") }
                .subscribe({
                    _eventListViewEntity.value = it
                    Timber.d("New event list successfully loaded")
                }, {
                    Timber.e(it, "Failed to observe event list")
                }, {
                    Timber.d("No longer observing event list")
                })
        )
    }

    fun onFilterPropertiesChanged(jobFilterProperties: JobFilterProperties) =
        onFilterPropertiesChangedListener(jobFilterProperties)

    fun onSortOrderChanged(groupByJob: Boolean, sortOrder: SortOrder) {
        if (eventListViewEntity.hasValue()) {
            _eventListViewEntity.value = eventListViewEntity.value!!.copy(
                groupByJob = groupByJob,
                sortOrder = sortOrder,
                events = eventListViewEntity.value!!.events.sortEventList(sortOrder)
            )
        } else {
            Timber.d("EventListViewEntity is empty")
        }
    }

    fun openCalendar(event: Event) {
        disposable.add(
            openCalendarUseCase.execute(event)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenCalendarRequestSuccessful.value = ShortLivedItem(true)
                    Timber.d("Successfully opened calendar")
                }, {
                    _isOpenCalendarRequestSuccessful.value = ShortLivedItem(false)
                    Timber.e(it, "Failed to open calendar")
                })
        )
    }

    fun openCalendar(eventListItem: EventListItem) =
        openCalendar(eventListItemToEventMapper.apply(eventListItem))

    private fun mapToEventListViewEntity(
        jobList: List<Job>,
        jobFilterProperties: JobFilterProperties
    ): EventListViewEntity {
        val currentGroupByJobValue =
            eventListViewEntity.value?.groupByJob ?: DEFAULT_GROUP_BY_JOB_VALUE

        val currentSortOrder = eventListViewEntity.value?.sortOrder ?: DEFAULT_SORT_ORDER

        return EventListViewEntity(
            jobFilterProperties = jobFilterProperties,
            groupByJob = currentGroupByJobValue,
            sortOrder = currentSortOrder,
            eventGroups = jobList.map(jobToEventGroupListItemMapper::apply),
            events = jobList.flatMap(jobToEventListItemsMapper::apply)
                .sortEventList(currentSortOrder)
        )
    }
}

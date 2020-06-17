package com.kapil.presentation.jobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.domain.jobs.CreateJobUseCase
import com.kapil.domain.jobs.LoadAndDeleteJobUseCase
import com.kapil.domain.jobs.ObserveJobListUseCase
import com.kapil.presentation.base.BaseViewModel
import com.kapil.presentation.common.ShortLivedItem
import com.kapil.presentation.common.hasValue
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber

class JobsViewModel(
    private val observeJobListUseCase: ObserveJobListUseCase,
    private val loadAndDeleteJobUseCase: LoadAndDeleteJobUseCase,
    private val createJobUseCase: CreateJobUseCase,
    private val mapper: JobToJobListItemMapper
) : BaseViewModel() {

    private val _jobListViewEntity = MutableLiveData<JobListViewEntity>()
    private val _isJobSuccessfullyDeleted = MutableLiveData<ShortLivedItem<Boolean>>()
    private val _isJobSuccessfullyRestored = MutableLiveData<ShortLivedItem<Boolean>>()

    val jobListViewEntity: LiveData<JobListViewEntity> = _jobListViewEntity
    val isJobSuccessfullyDeleted: LiveData<ShortLivedItem<Boolean>> = _isJobSuccessfullyDeleted
    val isJobSuccessfullyRestored: LiveData<ShortLivedItem<Boolean>> = _isJobSuccessfullyRestored

    private var recentlyDeletedJob: Job? = null

    private lateinit var onFilterPropertiesChangedListener: (JobFilterProperties) -> Unit

    private val filterPropertiesObservable = Flowable.create<JobFilterProperties>({ emitter ->
        if (!emitter.isCancelled) emitter.onNext(JobFilterProperties.DEFAULT)
        onFilterPropertiesChangedListener = { if (!emitter.isCancelled) emitter.onNext(it) }
    }, BackpressureStrategy.LATEST).subscribeOn(AndroidSchedulers.mainThread())

    fun observeJobList() {
        if (jobListViewEntity.hasValue()) {
            Timber.d("Already observing job list")
            return
        }
        disposable.add(
            filterPropertiesObservable
                .distinctUntilChanged()
                .applySchedulersAndLoadingObservableTo {
                    switchMap { jobFilterProperties ->
                        observeJobListUseCase.execute(jobFilterProperties)
                            .map { mapToJobListViewEntity(it, jobFilterProperties) }
                    }
                }
                .applyErrorObservable(::observeJobList)
                .doOnSubscribe { Timber.d("Observing job list") }
                .subscribe({
                    _jobListViewEntity.value = it
                    Timber.d("New job list successfully loaded")
                }, {
                    Timber.e(it, "Failed to observe job list")
                }, {
                    Timber.d("No longer observing job list")
                })
        )
    }

    fun onFilterPropertiesChanged(jobFilterProperties: JobFilterProperties) =
        onFilterPropertiesChangedListener(jobFilterProperties)

//    fun loadJobToDelete(jobId: Long) {
//        disposable.add(
//            loadJobUseCase.execute(jobId)
//                .applySchedulers()
//                .subscribe({
//                    _jobToDelete.value = ShortLivedItem(it)
//                    Timber.d("Job $jobId successfully loaded for deletion")
//                }, {
//                    Timber.e(it, "Failed to load job $jobId for deletion")
//                })
//        )
//    }

    fun deleteJob(jobId: Long) {
        disposable.add(
            loadAndDeleteJobUseCase.execute(jobId)
                .applySchedulers()
                .subscribe({
                    // Since the job list for the specified filter properties is already being
                    // observed, we do not have to update the job view entity here.
                    recentlyDeletedJob = it
                    _isJobSuccessfullyDeleted.value = ShortLivedItem(true)
                    Timber.d("Job $jobId successfully deleted")
                }, {
                    _isJobSuccessfullyDeleted.value = ShortLivedItem(false)
                    Timber.e(it, "Failed to delete job $jobId")
                })
        )
    }

    fun restoreRecentlyDeletedJob() {
        if (recentlyDeletedJob == null) {
            _isJobSuccessfullyRestored.value = ShortLivedItem(false)
            Timber.d("There is no job deleted recently")
            return
        }
        disposable.add(
            createJobUseCase.execute(recentlyDeletedJob!!)
                .applySchedulers()
                // Just to ensure that the job is not created twice.
                .doAfterTerminate { recentlyDeletedJob = null }
                .subscribe({
                    // Since the job list for the specified filter properties is already being
                    // observed, we do not have to update the job view entity here.
                    _isJobSuccessfullyRestored.value = ShortLivedItem(true)
                    Timber.d("Job ${recentlyDeletedJob!!.id} successfully restored")
                }, {
                    _isJobSuccessfullyRestored.value = ShortLivedItem(false)
                    Timber.e(it, "Failed to restore job ${recentlyDeletedJob!!.id}")
                })
        )
    }

    private fun mapToJobListViewEntity(
        jobList: List<Job>,
        jobFilterProperties: JobFilterProperties
    ): JobListViewEntity {
        val currentSortOrder = jobListViewEntity.value?.sortOrder ?: DEFAULT_SORT_ORDER
        return JobListViewEntity(
            jobFilterProperties = jobFilterProperties,
            sortOrder = currentSortOrder,
            jobs = jobList.map(mapper::apply).sortJobList(currentSortOrder)
        )
    }
}
package com.kapil.presentation.jobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.domain.jobs.CreateJobUseCase
import com.kapil.domain.jobs.DeleteJobUseCase
import com.kapil.domain.jobs.LoadJobUseCase
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
    private val loadJobUseCase: LoadJobUseCase,
    private val deleteJobUseCase: DeleteJobUseCase,
    private val createJobUseCase: CreateJobUseCase,
    private val mapper: JobToJobListItemMapper
) : BaseViewModel() {

    private val _jobListViewEntity = MutableLiveData<JobListViewEntity>()
    private val _jobToDelete = MutableLiveData<ShortLivedItem<Job>>()

    val jobListViewEntity: LiveData<JobListViewEntity> = _jobListViewEntity
    val jobToDelete: LiveData<ShortLivedItem<Job>> = _jobToDelete

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

    fun loadJobToDelete(jobId: Long) {
        disposable.add(
            loadJobUseCase.execute(jobId)
                .applySchedulers()
                .subscribe({
                    _jobToDelete.value = ShortLivedItem(it)
                    Timber.d("Job $jobId successfully loaded for deletion")
                }, {
                    Timber.e(it, "Failed to load job $jobId for deletion")
                })
        )
    }

    fun deleteJob(jobId: Long) {
        disposable.add(
            deleteJobUseCase.execute(jobId)
                .applySchedulers()
                .subscribe({
                    // Since the job list for the specified filter properties is already being
                    // observed, we do not have to update the job view entity here.
                    Timber.d("Job $jobId successfully deleted")
                }, {
                    Timber.e(it, "Failed to delete job $jobId")
                })
        )
    }

    fun restoreJob(job: Job) {
        disposable.add(
            createJobUseCase.execute(job)
                .applySchedulers()
                .subscribe({
                    // Since the job list for the specified filter properties is already being
                    // observed, we do not have to update the job view entity here.
                    Timber.d("Job ${job.id} successfully restored")
                }, {
                    Timber.e(it, "Failed to restore job ${job.id}")
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
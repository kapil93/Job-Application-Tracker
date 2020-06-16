package com.kapil.presentation.jobs.addoredit

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapil.domain.entity.Event
import com.kapil.domain.entity.Job
import com.kapil.domain.jobs.CreateJobUseCase
import com.kapil.domain.jobs.LoadJobUseCase
import com.kapil.domain.jobs.UpdateJobUseCase
import com.kapil.domain.openapp.*
import com.kapil.presentation.R
import com.kapil.presentation.base.BaseViewModel
import com.kapil.presentation.common.ShortLivedItem
import com.kapil.presentation.common.hasValue
import timber.log.Timber

class AddOrEditJobViewModel(
    private val loadJobUseCase: LoadJobUseCase,
    private val createJobUseCase: CreateJobUseCase,
    private val updateJobUseCase: UpdateJobUseCase,
    private val jobFormToJobMapper: JobFormToJobMapper,
    private val jobToJobFormMapper: JobToJobFormMapper,
    private val openBrowserUseCase: OpenBrowserUseCase,
    private val openMapsUseCase: OpenMapsUseCase,
    private val openEmailClientUseCase: OpenEmailClientUseCase,
    private val openDialerUseCase: OpenDialerUseCase,
    private val openCalendarUseCase: OpenCalendarUseCase
) : BaseViewModel() {

    private val _jobFormViewEntity = MutableLiveData<JobFormViewEntity>()
    private val _isCreateOrUpdateRequestSuccessful = MutableLiveData<ShortLivedItem<Boolean>>()
    private val _isOpenAppRequestSuccessful =
        MutableLiveData<ShortLivedItem<Pair<Boolean, AppType>>>()

    val jobFormViewEntity: LiveData<JobFormViewEntity> = _jobFormViewEntity
    val isCreateOrUpdateRequestSuccessful: LiveData<ShortLivedItem<Boolean>> =
        _isCreateOrUpdateRequestSuccessful
    val isOpenAppRequestSuccessful: LiveData<ShortLivedItem<Pair<Boolean, AppType>>> =
        _isOpenAppRequestSuccessful

    /**
     * This method will not update jobFormViewEntity live data if the live data is empty, i.e., the
     * view is freshly loading, AND if the job id is an illegal value, i.e., the user wants to
     * create a new job.
     */
    fun loadJob(jobId: Long) {
        if (jobFormViewEntity.hasValue()) {
            _jobFormViewEntity.value = jobFormViewEntity.value!!.copy(
                shouldUpdateView = true,
                showJobFormErrorMsg = false,
                showNewEventFormErrorMsg = false
            )
        } else {
            if (jobId != Job.ID_ILLEGAL_VALUE) {
                disposable.add(
                    loadJobUseCase.execute(jobId)
                        .map(jobToJobFormMapper)
                        .applySchedulers()
                        .applyLoadingObservable()
                        .applyErrorObservable { loadJob(jobId) }
                        .subscribe({
                            _jobFormViewEntity.value = JobFormViewEntity(
                                jobForm = it,
                                shouldUpdateView = true,
                                showJobFormErrorMsg = false,
                                showNewEventFormErrorMsg = false
                            )
                            Timber.d("Job for job id $jobId successfully loaded")
                        }, {
                            Timber.e(it, "Failed to load job for job id $jobId")
                        })
                )
            }
        }
    }

    fun onFormChanged(newJobForm: JobForm, viewUpdateRequired: Boolean = false) {
        _jobFormViewEntity.value = JobFormViewEntity(
            jobForm = newJobForm,
            shouldUpdateView = viewUpdateRequired,
            showJobFormErrorMsg = false,
            showNewEventFormErrorMsg = false
        )
    }

    fun onAddNewEvent(jobForm: JobForm) {
        if (jobForm.isNewEventFormValid) {
            _jobFormViewEntity.value = JobFormViewEntity(
                jobForm = jobForm.addNewEvent(),
                shouldUpdateView = true,
                showJobFormErrorMsg = false,
                showNewEventFormErrorMsg = false
            )
        } else {
            _jobFormViewEntity.value = JobFormViewEntity(
                jobForm = jobForm,
                shouldUpdateView = false,
                showJobFormErrorMsg = false,
                showNewEventFormErrorMsg = true
            )
        }
    }

    fun onRemoveRequestOfEvent(jobForm: JobForm, position: Int) {
        _jobFormViewEntity.value = JobFormViewEntity(
            jobForm = jobForm.removeEventAt(position),
            shouldUpdateView = true,
            showJobFormErrorMsg = false,
            showNewEventFormErrorMsg = false
        )
    }

    fun createJob(jobForm: JobForm) {
        if (jobForm.isFormValid) {
            createOrUpdateJob(
                toBeCreated = true,
                job = jobFormToJobMapper.apply(jobForm)
            )
        } else {
            _jobFormViewEntity.value = JobFormViewEntity(
                jobForm = jobForm,
                shouldUpdateView = false,
                showJobFormErrorMsg = true,
                showNewEventFormErrorMsg = false
            )
        }
    }

    fun updateJob(jobForm: JobForm) {
        if (jobForm.isFormValid) {
            createOrUpdateJob(
                toBeCreated = false,
                job = jobFormToJobMapper.apply(jobForm)
            )
        } else {
            _jobFormViewEntity.value = JobFormViewEntity(
                jobForm = jobForm,
                shouldUpdateView = false,
                showJobFormErrorMsg = true,
                showNewEventFormErrorMsg = false
            )
        }
    }

    private fun createOrUpdateJob(toBeCreated: Boolean, job: Job) {
        val createOrUpdateJobUseCase = if (toBeCreated) {
            createJobUseCase.execute(job)
        } else {
            updateJobUseCase.execute(job)
        }
        disposable.add(
            createOrUpdateJobUseCase
                .applySchedulers()
                .applyLoadingObservable()
                .applyErrorObservable { createOrUpdateJob(toBeCreated, job) }
                .subscribe({
                    _isCreateOrUpdateRequestSuccessful.value = ShortLivedItem(true)
                    Timber.d("Job successfully ${if (toBeCreated) "created" else "updated"}")
                }, {
                    _isCreateOrUpdateRequestSuccessful.value = ShortLivedItem(false)
                    Timber.e(it, "Failed to ${if (toBeCreated) "create" else "update"} Job")
                })
        )
    }

    fun openBrowser(url: String) {
        disposable.add(
            openBrowserUseCase.execute(url)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(true to AppType.BROWSER)
                    Timber.d("Successfully opened browser for url: $url")
                }, {
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(false to AppType.BROWSER)
                    Timber.e(it, "Failed to open browser for url: $url")
                })
        )
    }

    fun openMaps(address: String) {
        disposable.add(
            openMapsUseCase.execute(address)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(true to AppType.MAPS)
                    Timber.d("Successfully opened maps")
                }, {
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(false to AppType.MAPS)
                    Timber.e(it, "Failed to open maps")
                })
        )
    }

    fun openEmailClient(emailId: String) {
        disposable.add(
            openEmailClientUseCase.execute(emailId)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(true to AppType.EMAIL_CLIENT)
                    Timber.d("Successfully opened email client")
                }, {
                    _isOpenAppRequestSuccessful.value =
                        ShortLivedItem(false to AppType.EMAIL_CLIENT)
                    Timber.e(it, "Failed to open email client")
                })
        )
    }

    fun openDialer(phoneNumber: String) {
        disposable.add(
            openDialerUseCase.execute(phoneNumber)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(true to AppType.DIALER)
                    Timber.d("Successfully opened dialer")
                }, {
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(false to AppType.DIALER)
                    Timber.e(it, "Failed to open dialer")
                })
        )
    }

    fun openCalendar(event: Event) {
        disposable.add(
            openCalendarUseCase.execute(event)
                .applySchedulers()
                .applyLoadingObservable()
                .subscribe({
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(true to AppType.CALENDAR)
                    Timber.d("Successfully opened calendar")
                }, {
                    _isOpenAppRequestSuccessful.value = ShortLivedItem(false to AppType.CALENDAR)
                    Timber.e(it, "Failed to open calendar")
                })
        )
    }

    enum class AppType(@StringRes val errorMsg: Int) {
        BROWSER(R.string.prompt_open_browser_failed),
        MAPS(R.string.prompt_open_maps_failed),
        EMAIL_CLIENT(R.string.prompt_open_email_client_failed),
        DIALER(R.string.prompt_open_dialer_failed),
        CALENDAR(R.string.prompt_open_calendar_failed)
    }
}
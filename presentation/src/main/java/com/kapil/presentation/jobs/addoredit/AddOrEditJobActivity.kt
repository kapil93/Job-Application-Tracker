package com.kapil.presentation.jobs.addoredit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.TimePicker
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.kapil.domain.entity.Job
import com.kapil.presentation.R
import com.kapil.presentation.base.BaseActivity
import com.kapil.presentation.common.*
import kotlinx.android.synthetic.main.activity_add_or_edit_job.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.error_view.view.*
import kotlinx.android.synthetic.main.loading_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


class AddOrEditJobActivity : BaseActivity(), LoadingView, ErrorView {

    companion object {
        const val EXTRA_JOB_ID = "EXTRA_JOB_ID"
    }

    override val viewModel: AddOrEditJobViewModel by viewModel()

    private val defaultEventDuration by lazy {
        TimeUnit.MINUTES.toMillis(
            resources.getInteger(R.integer.default_event_duration_minutes).toLong()
        )
    }

    private val editTextList by lazy {
        listOf(
            role,
            companyName,
            companyNotes,
            contactName,
            appliedThrough,
            jobNotes,
            offerAmount,
            offerNotes,
            newEventTitle,
            newEventNotes
        )
    }

    private val actionableEditTextList by lazy {
        listOf(
            companyWebsite to viewModel::openBrowser,
            companyAddress to viewModel::openMaps,
            contactEmail to viewModel::openEmailClient,
            contactPhone to viewModel::openDialer
        )
    }

    private val dropdownViewList by lazy {
        listOf(
            status to R.array.status_array,
            priority to R.array.priority_array
        )
    }

    private val dateTimePickerEditTextList by lazy {
        listOf(newEventStartTime, newEventEndTime)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_job)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
            getString(if (isNewJob()) R.string.label_add_job_screen else R.string.label_edit_job_screen)

        if (!isNewJob()) loadingView.isVisible = true

        viewModel.loadJob(loadJobIdFromIntent())

        fab.setOnClickListener { submitForm() }

        eventsListView.adapter = JobEventListAdapter(JobEventListAdapter.Mode.EDIT).apply {
            onItemClickListener = { viewModel.openCalendar(currentList[it]) }
            onItemRemoveRequestListener = { removeEventAt(it) }
        }

        addEventButton.setOnClickListener { addNewEvent() }

        initEditTextListeners()
        initActionableEditTextListeners()
        initDropdownListeners()
        initDatePickerEditTextListeners()
        initDateTimePickerEditTextListeners()
        initSectionToggleBtnListeners()

        viewModel.jobFormViewEntity.observe(this, Observer(::updateViewEntity))
        viewModel.isCreateOrUpdateRequestSuccessful.observeShortLivedData(
            this, ::updateRequestResult
        )
        viewModel.isOpenAppRequestSuccessful.observeShortLivedData(this) {
            updateOpenAppRequestResult(it.first, it.second)
        }
        viewModel.isLoading.observe(this, Observer(::updateLoadingView))
        viewModel.isError.observe(this, Observer { updateErrorView(it.first, it.second) })
    }

    private fun updateViewEntity(jobFormViewEntity: JobFormViewEntity) {
        if (jobFormViewEntity.shouldUpdateView) setJobFormToView(jobFormViewEntity.jobForm)
        if (jobFormViewEntity.showJobFormErrorMsg) showMessage(jobFormViewEntity.jobForm.jobFormErrorMsg)
        if (jobFormViewEntity.showNewEventFormErrorMsg) showMessage(jobFormViewEntity.jobForm.newEventFormErrorMsg)
    }

    private fun submitForm() {
        closeKeyboard()
        val jobForm = getJobFormFromView()
        if (isNewJob()) {
            viewModel.createJob(jobForm)
        } else {
            viewModel.updateJob(jobForm)
        }
    }

    private fun getJobFormFromView() = JobForm(
        id = loadJobIdFromIntent(),
        role = role.text,
        companyName = companyName.text,
        companyWebsite = companyWebsite.text,
        companyAddress = companyAddress.text,
        companyNotes = companyNotes.text,
        contactName = contactName.text,
        contactEmail = contactEmail.text,
        contactPhone = contactPhone.text,
        priority = priority.selectedPosition.getPriorityFromIndex(),
        status = status.selectedPosition.getStatusFromIndex(),
        offerAmount = offerAmount.text,
        offerDateOfJoining = dateOfJoining.text,
        offerNotes = offerNotes.text,
        newEventTitle = newEventTitle.text,
        newEventStartTime = newEventStartTime.text,
        newEventEndTime = newEventEndTime.text,
        newEventNotes = newEventNotes.text,
        events = (eventsListView.adapter as JobEventListAdapter).currentList,
        appliedThrough = appliedThrough.text,
        notes = jobNotes.text
    )

    private fun setJobFormToView(jobForm: JobForm) {
        // Clear focus from edit texts to avoid triggering text change listeners
        clearFocusFromEditTexts()

        role.text = jobForm.role
        companyName.text = jobForm.companyName
        companyWebsite.text = jobForm.companyWebsite
        companyAddress.text = jobForm.companyAddress
        companyNotes.text = jobForm.companyNotes
        contactName.text = jobForm.contactName
        contactEmail.text = jobForm.contactEmail
        contactPhone.text = jobForm.contactPhone
        priority.selectedPosition = jobForm.priority.getIndexFromPriority()
        status.selectedPosition = jobForm.status.getIndexFromStatus()
        dateOfJoining.text = jobForm.offerDateOfJoining
        offerAmount.text = jobForm.offerAmount
        offerNotes.text = jobForm.offerNotes
        newEventTitle.text = jobForm.newEventTitle
        newEventStartTime.text = jobForm.newEventStartTime
        newEventEndTime.text = jobForm.newEventEndTime
        newEventNotes.text = jobForm.newEventNotes
        (eventsListView.adapter as JobEventListAdapter).submitList(jobForm.events.toMutableList())
        appliedThrough.text = jobForm.appliedThrough
        jobNotes.text = jobForm.notes

        offerDetailsGroup.isVisible = jobForm.showOfferSection
        showCompanyDetailsSection(jobForm.showCompanyDetailsSection)
        showContactDetailsSection(jobForm.isContactAvailable)
        labelEventList.isVisible = jobForm.showEventListSection
        showNewEventDetailsSection(jobForm.showNewEventDetailsSection)
    }

    private fun initEditTextListeners() {
        editTextList.forEach { textInputLayout ->
            textInputLayout.editText!!.doAfterTextChanged {
                if (textInputLayout.hasFocus()) {
                    viewModel.onFormChanged(getJobFormFromView())
                }
            }
        }
    }

    private fun initActionableEditTextListeners() {
        actionableEditTextList.forEach { pair ->
            val textInputLayout = pair.first
            val openApp = pair.second
            textInputLayout.setEndIconOnClickListener {
                openApp((textInputLayout.prefixText?.toString() ?: "") + textInputLayout.text)
            }
            textInputLayout.isEndIconVisible = false
            textInputLayout.editText!!.doAfterTextChanged {
                textInputLayout.isEndIconVisible = textInputLayout.text.isNotBlank()
                if (textInputLayout.hasFocus()) {
                    viewModel.onFormChanged(getJobFormFromView())
                }
            }
        }
    }

    private fun initDropdownListeners() {
        dropdownViewList.forEach { pair ->
            val textInputLayout = pair.first
            val dropdownView = textInputLayout.editText!! as MaterialAutoCompleteTextView
            val dropdownList = resources.getStringArray(pair.second)
            dropdownView.setAdapter(
                ArrayAdapter(this, android.R.layout.simple_list_item_1, dropdownList)
            )
            textInputLayout.selectedPosition = 0
            dropdownView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                viewModel.onFormChanged(getJobFormFromView(), textInputLayout.id == status.id)
            }
        }
    }

    private fun initDatePickerEditTextListeners() {
        val calendar = Calendar.getInstance()
        dateOfJoining.editText!!.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    dateOfJoining.editText!!.setText(
                        calendar.apply { set(year, month, dayOfMonth) }.time.formatDate()
                    )
                    viewModel.onFormChanged(getJobFormFromView())
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initDateTimePickerEditTextListeners() {
        val calendar = Calendar.getInstance()
        dateTimePickerEditTextList
            .forEach { textInputLayout ->
                textInputLayout.editText!!.setOnClickListener {
                    DatePickerDialog(
                        this,
                        { _, year, month, dayOfMonth ->
                            TimePickerDialog(
                                this@AddOrEditJobActivity,
                                { _: TimePicker, hourOfDay: Int, minute: Int ->
                                    textInputLayout.text = calendar.apply {
                                        set(year, month, dayOfMonth, hourOfDay, minute)
                                    }.time.formatDateTime()

                                    if (textInputLayout.id == newEventStartTime.id) {
                                        autoFillEndTime(calendar.time)
                                    }

                                    viewModel.onFormChanged(getJobFormFromView())
                                },
                                calendar.get(Calendar.HOUR_OF_DAY) + 1,
                                0,
                                false
                            ).show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
    }

    private fun initSectionToggleBtnListeners() {
        companyDetailsToggle.setOnCheckedChangeListener { toggleBtn, isChecked ->
            toggleBtn.showDrawableLeft(isChecked)
            companyDetailsGroup.isVisible = isChecked
        }
        contactDetailsToggle.setOnCheckedChangeListener { toggleBtn, isChecked ->
            toggleBtn.showDrawableLeft(isChecked)
            contactDetailsGroup.isVisible = isChecked
        }
        newEventDetailsToggle.setOnCheckedChangeListener { toggleBtn, isChecked ->
            toggleBtn.showDrawableLeft(isChecked)
            newEventDetailsGroup.isVisible = isChecked
        }
    }

    private fun autoFillEndTime(startTime: Date) {
        if (newEventEndTime.text.isBlank()) {
            newEventEndTime.text =
                Date(startTime.time + defaultEventDuration).formatDateTime()
        }
    }

    private fun addNewEvent() {
        closeKeyboard()
        viewModel.onAddNewEvent(getJobFormFromView())
    }

    private fun removeEventAt(position: Int) {
        viewModel.onRemoveRequestOfEvent(getJobFormFromView(), position)
    }

    private fun clearFocusFromEditTexts() = editTextList.forEach { it.editText!!.clearFocus() }

    private fun CompoundButton.showDrawableLeft(show: Boolean) {
        if (show)
            setCompoundDrawables(null, null, null, null)
        else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_add_circle,
                0,
                0,
                0
            )
        }
    }

    private fun updateRequestResult(isSuccessful: Boolean) {
        if (isSuccessful) {
            finish()
        } else {
            showMessage(if (isNewJob()) R.string.prompt_create_job_failed else R.string.prompt_update_job_failed)
        }
    }

    private fun updateOpenAppRequestResult(
        isSuccessful: Boolean,
        appType: AddOrEditJobViewModel.AppType
    ) {
        if (!isSuccessful) showMessage(appType.errorMsg)
    }

    private fun showCompanyDetailsSection(show: Boolean) {
        companyDetailsToggle.isChecked = show
    }

    private fun showContactDetailsSection(show: Boolean) {
        contactDetailsToggle.isChecked = show
    }

    private fun showNewEventDetailsSection(show: Boolean) {
        newEventDetailsToggle.isChecked = show
    }

    private fun loadJobIdFromIntent() = intent.getLongExtra(EXTRA_JOB_ID, Job.ID_ILLEGAL_VALUE)

    private fun isNewJob() = loadJobIdFromIntent() == Job.ID_ILLEGAL_VALUE

    override fun updateLoadingView(isLoading: Boolean) {
        loadingView.isVisible = isLoading
    }

    override fun updateErrorView(isError: Boolean, retry: (() -> Unit)) {
        errorView.isVisible = isError
        errorView.retryButton.setOnClickListener { retry() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        showDialog(
            R.string.close_add_or_edit_job_screen_dialog_title,
            R.string.close_add_or_edit_job_screen_dialog_message
        ) { super.onBackPressed() }
    }
}

package com.kapil.presentation.jobs.jobfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.presentation.R
import com.kapil.presentation.base.BaseFragment
import com.kapil.presentation.common.getFilteredSet
import com.kapil.presentation.common.observeShortLivedData
import com.kapil.presentation.common.setFilteredSet
import com.kapil.presentation.common.text
import kotlinx.android.synthetic.main.job_filter_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class JobFilterFragment : BaseFragment() {

    override val viewModel: JobFilterViewModel by viewModel()

    private val statusCheckboxes by lazy {
        listOf(
            statusToApplyCheckbox,
            statusAppliedCheckbox,
            statusInProcessCheckbox,
            statusDidNotWorkOutCheckbox,
            statusGotTheJobCheckbox
        )
    }

    private val priorityCheckboxes by lazy {
        listOf(
            priorityLowCheckbox,
            priorityMediumCheckbox,
            priorityHighCheckbox,
            priorityVeryHighCheckbox
        )
    }

    var onFilterPropertiesAppliedListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.job_filter_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyFilterBtn.setOnClickListener { onApplyClicked() }

        roleKeywordView.isEndIconVisible = false
        roleKeywordView.editText!!.doAfterTextChanged {
            if (!it.isNullOrBlank()) viewModel.loadRoleFilterKeywordResults(it.toString())
        }

        companyKeywordView.isEndIconVisible = false
        companyKeywordView.editText!!.doAfterTextChanged {
            if (!it.isNullOrBlank()) viewModel.loadCompanyFilterKeywordResults(it.toString())
        }

        viewModel.roleSearchResults.observeShortLivedData(
            viewLifecycleOwner, ::updateRoleFilterView
        )
        viewModel.companySearchResults.observeShortLivedData(
            viewLifecycleOwner, ::updateCompanyFilterView
        )
    }

    private fun updateRoleFilterView(roleList: List<String>) {
        roleKeywordView.updateAutoCompleteList(roleList)
    }

    private fun updateCompanyFilterView(companyList: List<String>) {
        companyKeywordView.updateAutoCompleteList(companyList)
    }

    private fun TextInputLayout.updateAutoCompleteList(list: List<String>) =
        (editText!! as AutoCompleteTextView).apply {
            setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list))
        }

    private fun onApplyClicked() {
        when {
            !isStatusFilterValid() -> showMessage(R.string.prompt_status_filter_invalid)
            !isPriorityFilterValid() -> showMessage(R.string.prompt_priority_filter_invalid)
            else -> onFilterPropertiesAppliedListener?.invoke()
        }
        closeKeyboard()
    }

    private fun isStatusFilterValid() = statusCheckboxes.getFilteredSet<Job.Status>().isNotEmpty()

    private fun isPriorityFilterValid() =
        priorityCheckboxes.getFilteredSet<Job.Priority>().isNotEmpty()

    fun getFilterProperties() = JobFilterProperties(
        statusSet = statusCheckboxes.getFilteredSet(),
        prioritySet = priorityCheckboxes.getFilteredSet(),
        companyKeyword = companyKeywordView.text.takeIf { it.isNotBlank() },
        roleKeyword = roleKeywordView.text.takeIf { it.isNotBlank() }
    )

    fun setFilterProperties(jobFilterProperties: JobFilterProperties) {
        statusCheckboxes.setFilteredSet(jobFilterProperties.statusSet)
        priorityCheckboxes.setFilteredSet(jobFilterProperties.prioritySet)
        companyKeywordView.editText!!.setText(jobFilterProperties.companyKeyword)
        roleKeywordView.editText!!.setText(jobFilterProperties.roleKeyword)
    }
}

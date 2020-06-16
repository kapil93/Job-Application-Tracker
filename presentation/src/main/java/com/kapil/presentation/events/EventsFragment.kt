package com.kapil.presentation.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.kapil.presentation.R
import com.kapil.presentation.common.ErrorView
import com.kapil.presentation.common.LoadingView
import com.kapil.presentation.common.observeShortLivedData
import com.kapil.presentation.home.BottomNavFragment
import com.kapil.presentation.jobs.jobfilter.JobFilterFragment
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.error_view.view.*
import kotlinx.android.synthetic.main.events_fragment.*
import kotlinx.android.synthetic.main.loading_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsFragment : BottomNavFragment(), LoadingView, ErrorView {

    override val viewModel: EventsViewModel by viewModel()

    override fun getTitle() = R.string.label_events_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observeEventList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventGroupListView.adapter = EventGroupListAdapter().apply {
            onEventItemClickListener = { viewModel.openCalendar(it) }
        }
        eventListView.adapter = EventListAdapter().apply {
            onItemClickListener = { viewModel.openCalendar(currentList[it]) }
        }

        filterBtn.setOnCheckedChangeListener { _, isChecked ->
            filterView.isVisible = isChecked
            groupByJobBtn.isVisible =
                !isChecked && ((eventGroupListView.adapter as EventGroupListAdapter).itemCount > 0)
            dateSortBtn.isVisible =
                !isChecked && ((eventListView.adapter as EventListAdapter).itemCount > 0)
            if (!isChecked) closeKeyboard()
        }

        getJobFilterFragment().onFilterPropertiesAppliedListener = {
            hideFilterView()
            viewModel.onFilterPropertiesChanged(getJobFilterFragment().getFilterProperties())
        }

        viewModel.eventListViewEntity.observe(viewLifecycleOwner, Observer(::updateViewEntity))
        viewModel.isOpenCalendarRequestSuccessful.observeShortLivedData(
            viewLifecycleOwner, ::updateOpenCalendarRequestResult
        )
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::updateLoadingView))
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            updateErrorView(it.first, it.second)
        })
    }

    override fun onResume() {
        super.onResume()
        initSortBtnListeners { viewModel.onSortOrderChanged(groupByJobBtn.isSelected, it) }
    }

    override fun onPause() {
        super.onPause()
        removeSortBtnListeners()
    }

    private fun updateViewEntity(eventListViewEntity: EventListViewEntity) {
        getJobFilterFragment().setFilterProperties(eventListViewEntity.jobFilterProperties)
        setSortOrderToSortBtns(eventListViewEntity.groupByJob, eventListViewEntity.sortOrder)
        showAptListView(eventListViewEntity.groupByJob)
        (eventGroupListView.adapter as EventGroupListAdapter).submitList(eventListViewEntity.eventGroups)
        (eventListView.adapter as EventListAdapter).submitList(eventListViewEntity.events)
        emptyListMsg.isVisible = eventListViewEntity.events.isEmpty()
        groupByJobBtn.isVisible = eventListViewEntity.events.isNotEmpty()
        dateSortBtn.isVisible = eventListViewEntity.events.isNotEmpty()
        filterBtn.isSelected = !eventListViewEntity.jobFilterProperties.isDefault()
    }

    private fun updateOpenCalendarRequestResult(isSuccessful: Boolean) {
        if (!isSuccessful) showMessage(R.string.prompt_open_calendar_failed)
    }

    private fun initSortBtnListeners(onSortOrderChanged: (SortOrder) -> Unit) {
        groupByJobBtn.setOnCheckedChangeListener { btn, _ ->
            if (!btn.isSelected) {
                btn.isSelected = true
                dateSortBtn.isSelected = false
                onSortOrderChanged(getSortOrderFromSortBtns())
            }
        }
        dateSortBtn.setOnCheckedChangeListener { btn, isChecked ->
            if (!btn.isSelected) btn.isChecked = !isChecked
            btn.isSelected = true
            groupByJobBtn.isSelected = false
            onSortOrderChanged(getSortOrderFromSortBtns())
        }
    }

    private fun removeSortBtnListeners() {
        groupByJobBtn.setOnCheckedChangeListener(null)
        dateSortBtn.setOnCheckedChangeListener(null)
    }

    private fun showAptListView(showGroupList: Boolean) {
        eventGroupListView.isVisible = showGroupList
        eventListView.isVisible = !showGroupList
    }

    private fun setSortOrderToSortBtns(groupByJob: Boolean, sortOrder: SortOrder) {
        if (groupByJob) {
            groupByJobBtn.isSelected = true
            dateSortBtn.isSelected = false
        } else {
            when (sortOrder) {
                SortOrder.DATE_ASC -> {
                    dateSortBtn.isSelected = true
                    dateSortBtn.isChecked = true
                    groupByJobBtn.isSelected = false
                }
                SortOrder.DATE_DESC -> {
                    dateSortBtn.isSelected = true
                    dateSortBtn.isChecked = false
                    groupByJobBtn.isSelected = false
                }
            }
        }
    }

    private fun getSortOrderFromSortBtns() = when {
        dateSortBtn.isSelected && dateSortBtn.isChecked -> SortOrder.DATE_ASC
        dateSortBtn.isSelected && !dateSortBtn.isChecked -> SortOrder.DATE_DESC
        // Any random value may go in else block as it is never expected to be executed
        else -> SortOrder.DATE_DESC
    }

    private fun hideFilterView() {
        filterBtn.isChecked = false
    }

    private fun getJobFilterFragment(): JobFilterFragment =
        (childFragmentManager.findFragmentByTag(filter_fragment_container.tag as String) as JobFilterFragment)

    override fun updateLoadingView(isLoading: Boolean) {
        loadingView.isVisible = isLoading
    }

    override fun updateErrorView(isError: Boolean, retry: (() -> Unit)) {
        errorView.isVisible = isError
        errorView.retryButton.setOnClickListener { retry() }
    }
}

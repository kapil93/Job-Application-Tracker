package com.kapil.presentation.jobs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kapil.domain.entity.Job
import com.kapil.presentation.R
import com.kapil.presentation.common.*
import com.kapil.presentation.home.BottomNavFragment
import com.kapil.presentation.jobs.addoredit.AddOrEditJobActivity
import com.kapil.presentation.jobs.jobfilter.JobFilterFragment
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.error_view.view.*
import kotlinx.android.synthetic.main.jobs_fragment.*
import kotlinx.android.synthetic.main.loading_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class JobsFragment : BottomNavFragment(), LoadingView, ErrorView {

    override val viewModel: JobsViewModel by viewModel()

    override fun getTitle() = R.string.label_jobs_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observeJobList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.jobs_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobsListView.adapter = JobListAdapter().apply {
            onItemClickListener = ::onJobListItemClicked
            // Required to set it true so that viewHolder.itemId returns corresponding job id.
            setHasStableIds(true)
        }
        jobsListView.addItemDecoration(
            getBottomMarginItemDecoration(
                requireContext().resources.getDimension(R.dimen.margin_xxxl).toInt()
            )
        )
        getItemSwipeDeleteHelper { viewModel.loadJobToDelete(it.itemId) }
            .attachToRecyclerView(jobsListView)

        fab.setOnClickListener { startActivity(Intent(context, AddOrEditJobActivity::class.java)) }

        filterBtn.setOnCheckedChangeListener { _, isChecked ->
            filterView.isVisible = isChecked
            if (!isChecked) closeKeyboard()
        }

        getJobFilterFragment().onFilterPropertiesAppliedListener = {
            hideFilterView()
            viewModel.onFilterPropertiesChanged(getJobFilterFragment().getFilterProperties())
        }

        viewModel.jobListViewEntity.observe(viewLifecycleOwner, Observer(::updateViewEntity))
        viewModel.jobToDelete.observeShortLivedData(viewLifecycleOwner, ::updateJobToDelete)
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::updateLoadingView))
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            updateErrorView(it.first, it.second)
        })
    }

    private fun updateViewEntity(jobListViewEntity: JobListViewEntity) {
        // Ignore sort order as it never changes
        getJobFilterFragment().setFilterProperties(jobListViewEntity.jobFilterProperties)
        (jobsListView.adapter as JobListAdapter).submitList(jobListViewEntity.jobs) {
            jobsListView.invalidateItemDecorations()
        }
        emptyListMsg.isVisible = jobListViewEntity.jobs.isEmpty()
        filterBtn.isSelected = !jobListViewEntity.jobFilterProperties.isDefault()
    }

    /**
     * Since this method contains code to trigger deletion of a resource and not just a UI update,
     * unnecessary triggers MUST be avoided as it may lead to inconsistency in the database.
     */
    private fun updateJobToDelete(job: Job) {
        viewModel.deleteJob(job.id)
        Snackbar.make(jobsListView, R.string.msg_job_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.btn_text_undo) {
                viewModel.restoreJob(job)
                showMessage(R.string.msg_job_restored)
            }
            .show()
    }

    private fun onJobListItemClicked(jobListItem: JobListItem) {
        startActivity(Intent(context, AddOrEditJobActivity::class.java).apply {
            putExtra(AddOrEditJobActivity.EXTRA_JOB_ID, jobListItem.id)
        })
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

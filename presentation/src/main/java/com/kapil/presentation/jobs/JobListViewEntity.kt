package com.kapil.presentation.jobs

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import java.util.*


val DEFAULT_SORT_ORDER = SortOrder.JOB_SORT_ORDER

data class JobListViewEntity(
    val jobFilterProperties: JobFilterProperties,
    val sortOrder: SortOrder,
    val jobs: List<JobListItem>
)

// For now, there is only one sort order
enum class SortOrder {
    JOB_SORT_ORDER
}

data class JobListItem(
    val id: Long,
    val role: String,
    val companyName: String,
    val priority: Job.Priority,
    val contactName: String = "",
    val address: String = "",
    val status: Job.Status
)

fun List<JobListItem>.sortJobList(sortOrder: SortOrder) = when (sortOrder) {
    SortOrder.JOB_SORT_ORDER -> sortedWith(
        compareByDescending<JobListItem> { it.priority.ordinal }
            .thenBy { it.status.ordinal }
            .thenBy { it.companyName.toLowerCase(Locale.getDefault()) }
            .thenBy { it.id }
    )
}
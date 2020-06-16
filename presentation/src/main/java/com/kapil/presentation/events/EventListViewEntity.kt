package com.kapil.presentation.events

import com.kapil.domain.entity.Event
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.presentation.common.parseDateTime


const val DEFAULT_GROUP_BY_JOB_VALUE = true
val DEFAULT_SORT_ORDER = SortOrder.DATE_DESC

data class EventListViewEntity(
    val jobFilterProperties: JobFilterProperties,
    val groupByJob: Boolean,
    val sortOrder: SortOrder,
    val eventGroups: List<EventGroupListItem>,
    val events: List<EventListItem>
)

enum class SortOrder {
    DATE_ASC,
    DATE_DESC
}

data class EventListItem(
    val jobId: Long,
    val title: String,
    val companyName: String,
    val role: String,
    val startTime: String,
    val endTime: String = "",
    val companyAddress: String = "",
    val notes: String = ""
)

data class EventGroupListItem(
    val jobId: Long,
    val companyName: String,
    val role: String,
    val companyAddress: String = "",
    val events: List<Event>
)

fun List<EventListItem>.sortEventList(sortOrder: SortOrder) = when (sortOrder) {
    SortOrder.DATE_ASC -> sortedBy { it.startTime.parseDateTime() }
    SortOrder.DATE_DESC -> sortedByDescending { it.startTime.parseDateTime() }
}
package com.kapil.presentation.offers

import java.util.*


val DEFAULT_SORT_ORDER = SortOrder.AMOUNT_DESC

data class OfferListViewEntity(
    val sortOrder: SortOrder,
    val offerList: List<OfferListItem>
)

enum class SortOrder {
    DOJ_ASC,
    DOJ_DESC,
    AMOUNT_ASC,
    AMOUNT_DESC
}

data class OfferListItem(
    val jobId: Long,
    val role: String,
    val companyName: String,
    val dateOfJoining: Date,
    val amount: String,
    val notes: String = ""
)

fun List<OfferListItem>.sortOfferList(sortOrder: SortOrder) = when (sortOrder) {
    SortOrder.DOJ_ASC -> sortedBy { it.dateOfJoining }
    SortOrder.DOJ_DESC -> sortedByDescending { it.dateOfJoining }
    SortOrder.AMOUNT_ASC -> sortedBy { it.amount.toNumericalValue() }
    SortOrder.AMOUNT_DESC -> sortedByDescending { it.amount.toNumericalValue() }
}

private fun String.toNumericalValue(): Int =
    filter { it.isDigit() }.let { if (it.isBlank()) 0 else it.toInt() }
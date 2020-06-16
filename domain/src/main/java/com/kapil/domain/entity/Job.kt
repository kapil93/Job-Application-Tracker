package com.kapil.domain.entity

data class Job(
    val id: Long = ID_ILLEGAL_VALUE,
    val role: String,
    val company: Company,
    val priority: Priority,
    val contact: Contact? = null,
    val status: Status,
    val offer: Offer? = null,
    val events: List<Event> = emptyList(),
    val appliedThrough: String? = null,
    val notes: String? = null
) {
    companion object {
        const val ID_ILLEGAL_VALUE = -1L
    }

    enum class Priority {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }

    enum class Status {
        TO_APPLY,
        APPLIED,
        IN_PROCESS,
        GOT_THE_JOB,
        DID_NOT_WORK_OUT
    }
}
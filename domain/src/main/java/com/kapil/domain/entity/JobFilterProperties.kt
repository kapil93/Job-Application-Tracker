package com.kapil.domain.entity

data class JobFilterProperties(
    val prioritySet: Set<Job.Priority>,
    val statusSet: Set<Job.Status>,
    // pass null value to remove role keyword filter
    val roleKeyword: String?,
    // pass null value to remove company keyword filter
    val companyKeyword: String?
) {
    companion object {
        val DEFAULT = JobFilterProperties(
            prioritySet = HashSet<Job.Priority>().apply { addAll(Job.Priority.values()) },
            statusSet = HashSet<Job.Status>().apply { addAll(Job.Status.values()) },
            roleKeyword = null,
            companyKeyword = null
        )
    }

    fun isDefault() = this == DEFAULT
}
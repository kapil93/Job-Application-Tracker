package com.kapil.domain.entity

import java.util.*

data class Event(
    val title: String,
    val startTime: Date,
    val endTime: Date? = null,
    val notes: String? = null
)
package com.kapil.domain.entity

import java.util.*

data class Offer(
    val amount: String,
    val dateOfJoining: Date,
    val notes: String? = null
)
package com.kapil.domain.entity

data class Company(
    val name: String,
    val website: String? = null,
    val address: String? = null,
    val notes: String? = null
)

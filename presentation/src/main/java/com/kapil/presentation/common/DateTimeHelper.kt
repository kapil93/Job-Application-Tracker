package com.kapil.presentation.common

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


private val dateFormat: DateFormat =
    DateFormat.getDateInstance(DateFormat.FULL).apply { isLenient = false }

private val dateTimeFormat: DateFormat =
    SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
        .apply { isLenient = false }

fun String.parseDate(): Date? =
    dateFormat.runCatching { parse(this@parseDate) }.getOrNull()

fun String.parseDateTime(): Date? =
    dateTimeFormat.runCatching { parse(this@parseDateTime) }.getOrNull()

fun Date.formatDate(): String = dateFormat.format(this)

fun Date.formatDateTime(): String = dateTimeFormat.format(this)
package com.kapil.presentation.common

import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ListAdapter
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputLayout
import com.kapil.domain.entity.Job
import com.kapil.presentation.R


fun Job.Priority.getResIdFromPriority() = when (this) {
    Job.Priority.LOW -> R.string.priority_low
    Job.Priority.MEDIUM -> R.string.priority_medium
    Job.Priority.HIGH -> R.string.priority_high
    Job.Priority.VERY_HIGH -> R.string.priority_very_high
}

fun Job.Status.getResIdFromStatus() = when (this) {
    Job.Status.TO_APPLY -> R.string.status_to_apply
    Job.Status.APPLIED -> R.string.status_applied
    Job.Status.IN_PROCESS -> R.string.status_in_process
    Job.Status.GOT_THE_JOB -> R.string.status_got_the_job
    Job.Status.DID_NOT_WORK_OUT -> R.string.status_did_not_work_out
}

fun Job.Status.getStatusColorResId() = when (this) {
    Job.Status.TO_APPLY -> R.color.status_to_apply
    Job.Status.APPLIED -> R.color.status_applied
    Job.Status.IN_PROCESS -> R.color.status_in_process
    Job.Status.GOT_THE_JOB -> R.color.status_got_the_job
    Job.Status.DID_NOT_WORK_OUT -> R.color.status_did_not_work_out
}

/**
 * The four methods declared below signify the mapping between the status and priority orders
 * between the string array declared in strings.xml and the enums declared in Job.kt
 *
 * Currently, the orders are the same.
 */
fun Int.getPriorityFromIndex() = Job.Priority.values()[this]

fun Job.Priority.getIndexFromPriority() = Job.Priority.values().indexOf(this)

fun Int.getStatusFromIndex() = Job.Status.values()[this]

fun Job.Status.getIndexFromStatus() = Job.Status.values().indexOf(this)

/**
 * The order of views must exactly match the order of the corresponding enum values.
 */
inline fun <reified T : Enum<T>> List<CheckBox>.getFilteredSet(): Set<T> =
    mapIndexed { i, checkbox -> Pair(i, checkbox.isChecked) }
        .filter { it.second }
        .map { enumValues<T>()[it.first] }
        .toHashSet()

/**
 * The order of views must exactly match the order of the corresponding enum values.
 */
inline fun <reified T : Enum<T>> List<CheckBox>.setFilteredSet(set: Set<T>) =
    forEachIndexed { i, checkbox -> checkbox.isChecked = enumValues<T>()[i] in set }

var TextInputLayout.text: String
    get() = editText!!.text.toString()
    set(value) = editText!!.setText(value)

/**
 * This property should ONLY be accessed when the child of TextInputLayout is AutoCompleteTextView
 */
var TextInputLayout.selectedPosition: Int
    get() = (editText!! as AutoCompleteTextView).adapter.toList().indexOf(text)
    set(value) {
        val dropdownView = (editText!! as AutoCompleteTextView)
        dropdownView.setText(dropdownView.adapter.getItem(value).toString(), false)
    }

fun ListAdapter.toList(): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until count) {
        list.add(getItem(i).toString())
    }
    return list.toList()
}

fun <T> LiveData<T>.hasValue() = value != null
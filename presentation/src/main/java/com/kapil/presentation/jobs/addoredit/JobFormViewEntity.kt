package com.kapil.presentation.jobs.addoredit

import androidx.annotation.StringRes
import com.kapil.domain.entity.Event
import com.kapil.domain.entity.Job
import com.kapil.presentation.R
import com.kapil.presentation.common.parseDateTime

data class JobFormViewEntity(
    val jobForm: JobForm,
    val shouldUpdateView: Boolean,
    val showJobFormErrorMsg: Boolean,
    val showNewEventFormErrorMsg: Boolean
)

data class JobForm(
    val id: Long,
    val role: String,
    val companyName: String,
    val companyWebsite: String,
    val companyAddress: String,
    val companyNotes: String,
    val priority: Job.Priority,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
    val status: Job.Status,
    val offerAmount: String,
    val offerDateOfJoining: String,
    val offerNotes: String,
    val newEventTitle: String,
    val newEventStartTime: String,
    val newEventEndTime: String,
    val newEventNotes: String,
    val events: List<Event> = emptyList(),
    val appliedThrough: String,
    val notes: String
) {
    private val isStatusGotTheJob: Boolean = status == Job.Status.GOT_THE_JOB

    private val isOfferFormValid: Boolean = !isStatusGotTheJob || (isStatusGotTheJob
            && offerAmount.isNotBlank() && offerDateOfJoining.isNotBlank())

    private val areNewEventTimesAndOrderValid: Boolean = run {
        val startDate = newEventStartTime.parseDateTime()
        val endDate = newEventEndTime.parseDateTime()
        return@run startDate != null && endDate != null && startDate < endDate
    }

    private val areBothNewEventTimesAvailable: Boolean =
        newEventStartTime.isNotBlank() && newEventEndTime.isNotBlank()

    private val areNewEventTimesAvailableAndInvalid =
        areBothNewEventTimesAvailable && !areNewEventTimesAndOrderValid

    val isNewEventFormValid: Boolean =
        newEventTitle.isNotBlank() && newEventStartTime.isNotBlank()
                && (newEventEndTime.isBlank() || areNewEventTimesAndOrderValid)

    val isFormValid: Boolean = role.isNotBlank() && companyName.isNotBlank() && isOfferFormValid

    val isContactAvailable: Boolean =
        (contactName.isNotBlank() || contactEmail.isNotBlank() || contactPhone.isNotBlank())

    val showCompanyDetailsSection: Boolean =
        (companyWebsite.isNotBlank() || companyAddress.isNotBlank() || companyNotes.isNotBlank())

    val showNewEventDetailsSection: Boolean =
        (newEventTitle.isNotBlank() || newEventStartTime.isNotBlank()
                || newEventEndTime.isNotBlank() || newEventNotes.isNotBlank())

    val isOfferAvailable: Boolean = isStatusGotTheJob && isOfferFormValid

    val showOfferSection: Boolean = isStatusGotTheJob

    val showEventListSection: Boolean = events.isNotEmpty()

    @StringRes
    val jobFormErrorMsg: Int = when {
        !isOfferFormValid -> R.string.prompt_offer_invalid
        !isFormValid -> R.string.prompt_form_invalid
        else -> R.string.prompt_form_invalid
    }

    @StringRes
    val newEventFormErrorMsg: Int = when {
        areNewEventTimesAvailableAndInvalid -> R.string.prompt_new_event_time_order_invalid
        !isNewEventFormValid -> R.string.prompt_new_event_invalid
        else -> R.string.prompt_new_event_invalid
    }
}

/**
 * The JobForm object to which this method is applied MUST contain a valid new event form.
 *
 * Otherwise,
 * @throws java.text.ParseException
 */
fun JobForm.addNewEvent(): JobForm = copy(
    newEventTitle = "",
    newEventStartTime = "",
    newEventEndTime = "",
    newEventNotes = "",
    events = events.addItemFollowingNaturalOrder(
        Event(
            title = newEventTitle,
            startTime = newEventStartTime.parseDateTime()!!,
            endTime = newEventEndTime.parseDateTime(),
            notes = newEventNotes
        )
    )
)

/**
 * The JobForm object to which this method is applied MUST contain the position supplied as argument.
 *
 * Otherwise,
 * @throws IndexOutOfBoundsException
 */
fun JobForm.removeEventAt(position: Int): JobForm = copy(
    events = events.toMutableList().apply { removeAt(position) }
)

private fun List<Event>.addItemFollowingNaturalOrder(event: Event): List<Event> =
    toMutableList().apply {
        var insertionPoint = binarySearch { if (it.startTime <= event.startTime) -1 else 1 }
        if (insertionPoint < 0) {
            insertionPoint = (-insertionPoint - 1)
        }
        add(insertionPoint, event)
    }
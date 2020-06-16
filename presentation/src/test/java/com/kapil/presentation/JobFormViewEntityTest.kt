package com.kapil.presentation

import androidx.annotation.StringRes
import com.kapil.domain.entity.Job
import com.kapil.presentation.jobs.addoredit.JobForm
import org.junit.Assert.assertEquals
import org.junit.Test


class JobFormViewEntityTest {

    @Test
    fun `test job form validity checks`() {
        dummyJobFormAndPropertyList.forEach { pair ->
            val actualJobForm = pair.first
            val expectedJobFormProperties = pair.second
            assertEquals(expectedJobFormProperties.isFormValid, actualJobForm.isFormValid)
            assertEquals(
                expectedJobFormProperties.isNewEventFormValid,
                actualJobForm.isNewEventFormValid
            )
            assertEquals(
                expectedJobFormProperties.showNewEventDetailsSection,
                actualJobForm.showNewEventDetailsSection
            )
            assertEquals(expectedJobFormProperties.jobFormErrorMsg, actualJobForm.jobFormErrorMsg)
            assertEquals(
                expectedJobFormProperties.newEventFormErrorMsg,
                actualJobForm.newEventFormErrorMsg
            )
        }
    }

    private val dummyJobFormAndPropertyList = listOf(
        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.DID_NOT_WORK_OUT,
            offerAmount = "",
            offerDateOfJoining = "",
            offerNotes = "",
            newEventTitle = "",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = false,
            showNewEventDetailsSection = false,
            isFormValid = false,
            jobFormErrorMsg = R.string.prompt_form_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_invalid
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "New event title",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = false,
            showNewEventDetailsSection = true,
            isFormValid = false,
            jobFormErrorMsg = R.string.prompt_form_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_invalid
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "New event title",
            newEventStartTime = "Sunday, June 14, 2020 3:00 PM",
            newEventEndTime = "Sunday, June 14, 2020 3:00 PM",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = false,
            showNewEventDetailsSection = true,
            isFormValid = false,
            jobFormErrorMsg = R.string.prompt_offer_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_time_order_invalid
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "$1000000",
            offerDateOfJoining = "",
            offerNotes = "Offer notes",
            newEventTitle = "New event title",
            newEventStartTime = "Sunday, June 14, 2020 3:00 PM",
            newEventEndTime = "",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = true,
            showNewEventDetailsSection = true,
            isFormValid = false,
            jobFormErrorMsg = R.string.prompt_offer_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_invalid
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "New event title",
            newEventStartTime = "Sunday, June 14, 2020 3:00 PM",
            newEventEndTime = "Sunday, June 14, 2020 2:00 PM",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = false,
            showNewEventDetailsSection = true,
            isFormValid = true,
            jobFormErrorMsg = R.string.prompt_form_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_time_order_invalid
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "Company address",
            companyNotes = "Company notes",
            priority = Job.Priority.VERY_HIGH,
            contactName = "Contact name",
            contactEmail = "Contact email",
            contactPhone = "Contact phone",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "New event title",
            newEventStartTime = "Sunday, June 14, 2020 3:00 PM",
            newEventEndTime = "Sunday, June 14, 2020 6:00 PM",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to JobFormProperties(
            isNewEventFormValid = true,
            showNewEventDetailsSection = true,
            isFormValid = true,
            jobFormErrorMsg = R.string.prompt_form_invalid,
            newEventFormErrorMsg = R.string.prompt_new_event_invalid
        )
    )

    private data class JobFormProperties(
        val isNewEventFormValid: Boolean,
        val showNewEventDetailsSection: Boolean,
        val isFormValid: Boolean,
        @StringRes val jobFormErrorMsg: Int,
        @StringRes val newEventFormErrorMsg: Int
    )
}
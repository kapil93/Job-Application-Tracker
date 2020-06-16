package com.kapil.presentation

import com.kapil.domain.entity.*
import com.kapil.presentation.jobs.addoredit.JobForm
import com.kapil.presentation.jobs.addoredit.JobFormToJobMapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


open class JobFormToJobMapperValidCaseTest {

    private lateinit var mapper: JobFormToJobMapper

    @Before
    fun setup() {
        mapper = JobFormToJobMapper()
    }

    @Test
    fun `test mapper if form valid`() {
        dummyValidJobFormAndJobList.map { mapper.apply(it.first) to it.second }.forEach { pair ->
            val actualJob = pair.first
            val expectedJob = pair.second
            assertEquals(expectedJob, actualJob)
        }
    }

    private val dummyValidJobFormAndJobList = listOf(
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
            newEventTitle = "",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = listOf(
                Event(
                    title = "Event title",
                    startTime = Date(0),
                    endTime = Date(1),
                    notes = "Event notes"
                )
            ),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "Company website",
                address = "Company address",
                notes = "Company notes"
            ),
            contact = Contact(
                name = "Contact name",
                email = "Contact email",
                phone = "Contact phone"
            ),
            priority = Job.Priority.VERY_HIGH,
            status = Job.Status.GOT_THE_JOB,
            offer = Offer(
                amount = "$1000000",
                dateOfJoining = Date(1592085600000),
                notes = "Offer notes"
            ),
            appliedThrough = "LinkedIn",
            notes = "Job notes",
            events = listOf(
                Event(
                    title = "Event title",
                    startTime = Date(0),
                    endTime = Date(1),
                    notes = "Event notes"
                )
            )
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
            status = Job.Status.DID_NOT_WORK_OUT,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "Company website",
                address = "Company address",
                notes = "Company notes"
            ),
            contact = Contact(
                name = "Contact name",
                email = "Contact email",
                phone = "Contact phone"
            ),
            priority = Job.Priority.VERY_HIGH,
            status = Job.Status.DID_NOT_WORK_OUT,
            offer = null,
            appliedThrough = "LinkedIn",
            notes = "Job notes",
            events = emptyList()
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "Company website",
            companyAddress = "",
            companyNotes = "",
            priority = Job.Priority.MEDIUM,
            contactName = "",
            contactEmail = "Contact email",
            contactPhone = "",
            status = Job.Status.TO_APPLY,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = listOf(
                Event(
                    title = "Event title",
                    startTime = Date(0),
                    endTime = null,
                    notes = null
                )
            ),
            appliedThrough = "LinkedIn",
            notes = "Job notes"
        ) to Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "Company website",
                address = null,
                notes = null
            ),
            contact = Contact(
                name = null,
                email = "Contact email",
                phone = null
            ),
            priority = Job.Priority.MEDIUM,
            status = Job.Status.TO_APPLY,
            offer = null,
            appliedThrough = "LinkedIn",
            notes = "Job notes",
            events = listOf(
                Event(
                    title = "Event title",
                    startTime = Date(0),
                    endTime = null,
                    notes = null
                )
            )
        ),

        JobForm(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            companyName = "Company name",
            companyWebsite = "",
            companyAddress = "",
            companyNotes = "",
            priority = Job.Priority.LOW,
            contactName = "",
            contactEmail = "",
            contactPhone = "",
            status = Job.Status.GOT_THE_JOB,
            offerAmount = "$1000000",
            offerDateOfJoining = "Sunday, June 14, 2020",
            offerNotes = "Offer notes",
            newEventTitle = "",
            newEventStartTime = "",
            newEventEndTime = "",
            newEventNotes = "",
            events = emptyList(),
            appliedThrough = "LinkedIn",
            notes = ""
        ) to Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = null,
                address = null,
                notes = null
            ),
            contact = null,
            priority = Job.Priority.LOW,
            status = Job.Status.GOT_THE_JOB,
            offer = Offer(
                amount = "$1000000",
                dateOfJoining = Date(1592085600000),
                notes = "Offer notes"
            ),
            appliedThrough = "LinkedIn",
            notes = null,
            events = emptyList()
        )
    )
}
package com.kapil.presentation

import com.kapil.domain.entity.Job
import com.kapil.presentation.jobs.addoredit.JobForm
import com.kapil.presentation.jobs.addoredit.JobFormToJobMapper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
open class JobFormToJobMapperInvalidCaseTest {

    private lateinit var mapper: JobFormToJobMapper

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    @Parameterized.Parameter
    @JvmField
    var input: JobForm? = null

    @Before
    fun setup() {
        mapper = JobFormToJobMapper()
    }

    @Test
    fun `test mapper if form invalid`() {
        expectedException.expect(IllegalArgumentException::class.java)
        mapper.apply(input!!)
    }

    companion object {
        @Parameterized.Parameters(name = "test for {index}")
        @JvmStatic
        fun data() = listOf(
            JobForm(
                id = Job.ID_ILLEGAL_VALUE,
                role = "",
                companyName = "Company name",
                companyWebsite = "",
                companyAddress = "",
                companyNotes = "",
                priority = Job.Priority.VERY_HIGH,
                contactName = "",
                contactEmail = "",
                contactPhone = "",
                status = Job.Status.APPLIED,
                offerAmount = "",
                offerDateOfJoining = "",
                offerNotes = "",
                newEventTitle = "",
                newEventStartTime = "",
                newEventEndTime = "",
                newEventNotes = "",
                events = emptyList(),
                appliedThrough = "",
                notes = ""
            ),

            JobForm(
                id = Job.ID_ILLEGAL_VALUE,
                role = "Role",
                companyName = "",
                companyWebsite = "Company website",
                companyAddress = "",
                companyNotes = "",
                priority = Job.Priority.VERY_HIGH,
                contactName = "",
                contactEmail = "",
                contactPhone = "",
                status = Job.Status.APPLIED,
                offerAmount = "",
                offerDateOfJoining = "",
                offerNotes = "",
                newEventTitle = "",
                newEventStartTime = "",
                newEventEndTime = "",
                newEventNotes = "",
                events = emptyList(),
                appliedThrough = "",
                notes = ""
            ),

            JobForm(
                id = Job.ID_ILLEGAL_VALUE,
                role = "Role",
                companyName = "Company name",
                companyWebsite = "",
                companyAddress = "",
                companyNotes = "",
                priority = Job.Priority.VERY_HIGH,
                contactName = "",
                contactEmail = "",
                contactPhone = "",
                status = Job.Status.GOT_THE_JOB,
                offerAmount = "",
                offerDateOfJoining = "",
                offerNotes = "",
                newEventTitle = "",
                newEventStartTime = "",
                newEventEndTime = "",
                newEventNotes = "",
                events = emptyList(),
                appliedThrough = "",
                notes = ""
            )
        )
    }
}
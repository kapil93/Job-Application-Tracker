package com.kapil.presentation

import com.kapil.domain.entity.*
import com.kapil.presentation.jobs.addoredit.JobToJobFormMapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class JobToJobFormMapperTest {

    private lateinit var mapper: JobToJobFormMapper

    @Before
    fun setup() {
        mapper = JobToJobFormMapper()
    }

    @Test
    fun `test company section logic`() {
        dummyJobAndJobFormPropertyList.map { mapper.apply(it.first) to it.second }.forEach { pair ->
            val actualJobForm = pair.first
            val expectedJobFormProperties = pair.second
            assertEquals(
                expectedJobFormProperties.showCompanyDetailsSection,
                actualJobForm.showCompanyDetailsSection
            )
        }
    }

    @Test
    fun `test contact section logic`() {
        dummyJobAndJobFormPropertyList.map { mapper.apply(it.first) to it.second }.forEach { pair ->
            val actualJobForm = pair.first
            val expectedJobFormProperties = pair.second
            assertEquals(
                expectedJobFormProperties.isContactAvailable,
                actualJobForm.isContactAvailable
            )
        }
    }

    @Test
    fun `test offer section logic`() {
        dummyJobAndJobFormPropertyList.map { mapper.apply(it.first) to it.second }.forEach { pair ->
            val actualJobForm = pair.first
            val expectedJobFormProperties = pair.second
            assertEquals(expectedJobFormProperties.isOfferAvailable, actualJobForm.isOfferAvailable)
            assertEquals(expectedJobFormProperties.showOfferSection, actualJobForm.showOfferSection)
        }
    }

    @Test
    fun `test event list section logic`() {
        dummyJobAndJobFormPropertyList.map { mapper.apply(it.first) to it.second }.forEach { pair ->
            val actualJobForm = pair.first
            val expectedJobFormProperties = pair.second
            assertEquals(
                expectedJobFormProperties.showEventListSection,
                actualJobForm.showEventListSection
            )
        }
    }

    private val dummyJobAndJobFormPropertyList = listOf(
        Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "https://companywebsite.com",
                address = "",
                notes = ""
            ),
            contact = Contact(
                name = "Contact name",
                email = "",
                phone = ""
            ),
            priority = Job.Priority.VERY_HIGH,
            status = Job.Status.GOT_THE_JOB,
            offer = Offer(
                amount = "$1000000",
                dateOfJoining = Date(0),
                notes = ""
            ),
            appliedThrough = "LinkedIn",
            notes = "",
            events = emptyList()
        ) to JobFormProperties(
            showCompanyDetailsSection = true,
            isContactAvailable = true,
            isOfferAvailable = true,
            showOfferSection = true,
            showEventListSection = false
        ),

        Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "",
                address = "",
                notes = ""
            ),
            contact = Contact(
                name = "Contact name",
                email = "",
                phone = ""
            ),
            priority = Job.Priority.VERY_HIGH,
            status = Job.Status.GOT_THE_JOB,
            offer = null,
            appliedThrough = "LinkedIn",
            notes = "",
            events = listOf(
                Event(
                    title = "Event title",
                    startTime = Date(0),
                    endTime = Date(1),
                    notes = ""
                )
            )
        ) to JobFormProperties(
            showCompanyDetailsSection = false,
            isContactAvailable = true,
            isOfferAvailable = false,
            showOfferSection = true,
            showEventListSection = true
        ),

        Job(
            id = Job.ID_ILLEGAL_VALUE,
            role = "Role",
            company = Company(
                name = "Company name",
                website = "https://companywebsite.com",
                address = "",
                notes = ""
            ),
            contact = null,
            priority = Job.Priority.VERY_HIGH,
            status = Job.Status.APPLIED,
            offer = null,
            appliedThrough = "LinkedIn",
            notes = "",
            events = emptyList()
        ) to JobFormProperties(
            showCompanyDetailsSection = true,
            isContactAvailable = false,
            isOfferAvailable = false,
            showOfferSection = false,
            showEventListSection = false
        )
    )

    private data class JobFormProperties(
        val showCompanyDetailsSection: Boolean,
        val isContactAvailable: Boolean,
        val isOfferAvailable: Boolean,
        val showOfferSection: Boolean,
        val showEventListSection: Boolean
    )
}
package com.kapil.presentation.jobs.addoredit

import com.kapil.domain.entity.Company
import com.kapil.domain.entity.Contact
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.Offer
import com.kapil.presentation.common.parseDate
import io.reactivex.rxjava3.functions.Function

/**
 * The JobForm object passed to the apply method of this class is STRICTLY expected to be a valid
 * form.
 *
 * Otherwise,
 * @throws IllegalStateException
 */
class JobFormToJobMapper : Function<JobForm, Job> {

    override fun apply(jobForm: JobForm): Job = if (jobForm.isFormValid) {
        Job(
            id = jobForm.id,
            role = jobForm.role,
            company = Company(
                name = jobForm.companyName,
                website = jobForm.companyWebsite.takeIf { it.isNotBlank() },
                address = jobForm.companyAddress.takeIf { it.isNotBlank() },
                notes = jobForm.companyNotes.takeIf { it.isNotBlank() }
            ),
            contact = if (jobForm.isContactAvailable) Contact(
                name = jobForm.contactName.takeIf { it.isNotBlank() },
                email = jobForm.contactEmail.takeIf { it.isNotBlank() },
                phone = jobForm.contactPhone.takeIf { it.isNotBlank() }
            ) else null,
            priority = jobForm.priority,
            status = jobForm.status,
            offer = if (jobForm.isOfferAvailable) Offer(
                amount = jobForm.offerAmount,
                dateOfJoining = jobForm.offerDateOfJoining.parseDate()!!,
                notes = jobForm.offerNotes.takeIf { it.isNotBlank() }
            ) else null,
            events = jobForm.events,
            appliedThrough = jobForm.appliedThrough.takeIf { it.isNotBlank() },
            notes = jobForm.notes.takeIf { it.isNotBlank() }
        )
    } else {
        throw IllegalArgumentException("The JobForm object passed is not a valid form")
    }
}
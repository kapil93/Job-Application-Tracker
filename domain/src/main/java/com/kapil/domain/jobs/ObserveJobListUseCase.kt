package com.kapil.domain.jobs

import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import io.reactivex.rxjava3.core.Flowable

class ObserveJobListUseCase(private val jobsRepository: JobsRepository) {

    fun execute(jobFilterProperties: JobFilterProperties): Flowable<List<Job>> =
        when {
            jobFilterProperties.statusSet.isEmpty() || jobFilterProperties.prioritySet.isEmpty() ->
                Flowable.error(IllegalArgumentException("Status Set and Priority Set of Filter Properties cannot be empty"))
            else -> jobsRepository.observeJobList(jobFilterProperties)
        }
}

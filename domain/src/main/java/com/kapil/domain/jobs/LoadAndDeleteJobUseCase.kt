package com.kapil.domain.jobs

import com.kapil.domain.entity.Job
import io.reactivex.rxjava3.core.Single

/**
 * This use case returns the deleted job so that the presentation layer has the option to restore
 * the job if needed.
 */
class LoadAndDeleteJobUseCase(
    private val loadJobUseCase: LoadJobUseCase,
    private val deleteJobUseCase: DeleteJobUseCase
) {
    fun execute(jobId: Long): Single<Job> = loadJobUseCase.execute(jobId).flatMap {
        deleteJobUseCase.execute(jobId).andThen(Single.just(it))
    }
}

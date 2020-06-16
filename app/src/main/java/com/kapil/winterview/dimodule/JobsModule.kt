package com.kapil.winterview.dimodule

import com.kapil.data.common.JobDbToJobMapper
import com.kapil.data.common.JobToJobDbMapper
import com.kapil.data.jobs.JobsDbDataSource
import com.kapil.data.jobs.JobsRepositoryImpl
import com.kapil.data.jobs.KeywordSearchDbDataSource
import com.kapil.data.jobs.KeywordSearchRepositoryImpl
import com.kapil.data.jobs.realmdb.RealmJobsDataSource
import com.kapil.data.jobs.realmdb.RealmKeywordSearchDataSource
import com.kapil.domain.jobs.*
import com.kapil.domain.jobs.keywordsearch.KeywordSearchRepository
import com.kapil.domain.jobs.keywordsearch.LoadCompanySearchResultsUseCase
import com.kapil.domain.jobs.keywordsearch.LoadRoleSearchResultsUseCase
import com.kapil.presentation.R
import com.kapil.presentation.jobs.JobToJobListItemMapper
import com.kapil.presentation.jobs.JobsViewModel
import com.kapil.presentation.jobs.addoredit.AddOrEditJobViewModel
import com.kapil.presentation.jobs.addoredit.JobFormToJobMapper
import com.kapil.presentation.jobs.addoredit.JobToJobFormMapper
import com.kapil.presentation.jobs.jobfilter.JobFilterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val jobsModule = module {

    viewModel {
        JobsViewModel(
            observeJobListUseCase = get(),
            loadJobUseCase = get(),
            deleteJobUseCase = get(),
            createJobUseCase = get(),
            mapper = get()
        )
    }
    viewModel {
        AddOrEditJobViewModel(
            loadJobUseCase = get(),
            createJobUseCase = get(),
            updateJobUseCase = get(),
            jobFormToJobMapper = get(),
            jobToJobFormMapper = get(),
            openBrowserUseCase = get(),
            openMapsUseCase = get(),
            openEmailClientUseCase = get(),
            openDialerUseCase = get(),
            openCalendarUseCase = get()
        )
    }
    viewModel { JobFilterViewModel(get(), get()) }

    single { ObserveJobListUseCase(get()) }
    single { LoadRoleSearchResultsUseCase(get()) }
    single {
        LoadCompanySearchResultsUseCase(
            get()
        )
    }
    single { LoadJobUseCase(get()) }
    single { CreateJobUseCase(get()) }
    single { UpdateJobUseCase(get()) }
    single { DeleteJobUseCase(get()) }

    single { JobToJobListItemMapper() }
    single { JobFormToJobMapper() }
    single { JobToJobFormMapper() }

    single { JobToJobDbMapper() }
    single { JobDbToJobMapper() }

    single<JobsDbDataSource> { RealmJobsDataSource(get(), get()) }
    single<KeywordSearchDbDataSource> {
        RealmKeywordSearchDataSource(
            androidContext().resources.getInteger(R.integer.keyword_search_result_count_limit)
        )
    }

    single<JobsRepository> { JobsRepositoryImpl(get()) }
    single<KeywordSearchRepository> { KeywordSearchRepositoryImpl(get()) }
}
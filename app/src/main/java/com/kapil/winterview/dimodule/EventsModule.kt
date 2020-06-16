package com.kapil.winterview.dimodule

import com.kapil.data.events.EventsDbDataSource
import com.kapil.data.events.EventsRepositoryImpl
import com.kapil.data.events.realmdb.RealmEventsDataSource
import com.kapil.domain.events.EventsRepository
import com.kapil.domain.events.ObserveJobsWithEventsUseCase
import com.kapil.presentation.events.EventListItemToEventMapper
import com.kapil.presentation.events.EventsViewModel
import com.kapil.presentation.events.JobToEventGroupListItemMapper
import com.kapil.presentation.events.JobToEventListItemsMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {

    viewModel {
        EventsViewModel(
            observeJobsWithEventsUseCase = get(),
            openCalendarUseCase = get(),
            jobToEventGroupListItemMapper = get(),
            jobToEventListItemsMapper = get(),
            eventListItemToEventMapper = get()
        )
    }

    single { ObserveJobsWithEventsUseCase(get()) }

    single { JobToEventGroupListItemMapper() }
    single { JobToEventListItemsMapper() }
    single { EventListItemToEventMapper() }

    single<EventsDbDataSource> { RealmEventsDataSource(get()) }

    single<EventsRepository> { EventsRepositoryImpl(get()) }
}
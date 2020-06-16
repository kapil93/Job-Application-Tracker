package com.kapil.winterview.dimodule

import com.kapil.data.offers.OffersDbDataSource
import com.kapil.data.offers.OffersRepositoryImpl
import com.kapil.data.offers.realmdb.RealmOffersDataSource
import com.kapil.domain.offers.ObserveJobsWithOfferUseCase
import com.kapil.domain.offers.OffersRepository
import com.kapil.presentation.offers.JobToOfferListItemMapper
import com.kapil.presentation.offers.OffersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val offersModule = module {

    viewModel { OffersViewModel(get(), get()) }

    single { JobToOfferListItemMapper() }

    single { ObserveJobsWithOfferUseCase(get()) }

    single<OffersDbDataSource> { RealmOffersDataSource(get()) }

    single<OffersRepository> { OffersRepositoryImpl(get()) }
}
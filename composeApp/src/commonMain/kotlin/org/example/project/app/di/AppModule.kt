package org.example.project.app.di

import org.example.project.app.data.AppRepositoryImpl
import org.example.project.app.domain.AppRepository
import org.example.project.app.presentation.AppViewModel
import org.example.project.core.data.cache.DataStoreManager

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory<AppRepository> { AppRepositoryImpl(get()) }
    factory<DataStoreManager.Read> { DataStoreManager.Base(dataStore = (get())) }
    viewModel {
        AppViewModel(
            runAsync = get(),
            appRepository = get(),
        )
    }
}

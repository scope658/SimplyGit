package org.example.project.app.di

import org.example.project.app.data.AppRepositoryImpl
import org.example.project.app.domain.AppRepository
import org.example.project.app.presentation.AppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppRepository> { AppRepositoryImpl(get()) }
    viewModel {
        AppViewModel(
            runAsync = get(),
            appRepository = get(),
        )
    }
}

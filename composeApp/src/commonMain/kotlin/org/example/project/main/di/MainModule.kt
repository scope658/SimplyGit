package org.example.project.main.di

import org.example.project.main.data.ListRepositoryImpl
import org.example.project.main.domain.ListRepository
import org.example.project.main.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<ListRepository> { ListRepositoryImpl() }
    viewModel {
        MainViewModel(
            runAsync = get(),
            repository = get(),
        )
    }
}
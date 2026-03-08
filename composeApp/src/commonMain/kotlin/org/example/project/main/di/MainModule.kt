package org.example.project.main.di

import org.example.project.main.data.MainRepositoryImpl
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.domain.MainRepository
import org.example.project.main.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<GithubApi> { GithubApi.Base(get()) }
    single<MainRepository> { MainRepositoryImpl(get()) }
    viewModel {
        MainViewModel(
            runAsync = get(),
            repository = get(),
            savedStateHandle = get(),
        )
    }
}

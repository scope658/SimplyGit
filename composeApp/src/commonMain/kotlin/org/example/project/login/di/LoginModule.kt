package org.example.project.login.di

import org.example.project.core.data.cache.DataStoreManager
import org.example.project.login.data.LoginRepositoryImpl
import org.example.project.login.domain.LoginRepository
import org.example.project.login.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<DataStoreManager.SaveToken> { DataStoreManager.Base(get()) }
    factory<LoginRepository> {
        LoginRepositoryImpl(
            get(), get(),
            customRunCatching = get()
        )
    }
    viewModel {
        LoginViewModel(
            savedStateHandle = get(),
            runAsync = get(),
            loginRepository = get(),
            manageResource = get(),
        )
    }
}

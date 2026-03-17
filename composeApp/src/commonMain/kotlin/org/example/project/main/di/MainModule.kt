package org.example.project.main.di

import org.example.project.core.cache.DataStoreManager
import org.example.project.core.cache.db.AppDatabase
import org.example.project.main.data.MainRepositoryImpl
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.HandleMainRequest
import org.example.project.main.domain.HandleUserRepoRequest
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.PagedResult
import org.example.project.main.presentation.MainUiMapper
import org.example.project.main.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<DataStoreManager.ReadToken> { DataStoreManager.Base(dataStore = get()) }
    single<MainRepository> {
        MainRepositoryImpl(
            githubApi = get(), dataStoreManager = get(),
            dao = get()
        )
    }
    single<PagedResult.Mapper<MainUiState>> { MainUiMapper() }
    single<HandleMainRequest> { HandleMainRequest.Base() }
    factory<HandleUserRepoRequest> { HandleUserRepoRequest.Base() }
    single<MainRepository> { MainRepositoryImpl(githubApi = get(), dataStoreManager = get()) }
    single<GithubApi> { GithubApiImpl(get()) }
    single<MainRepository> { MainRepositoryImpl(get()) }
    single<PagedResult.Mapper> { MainUiMapper() }
    factory<HandleUserRepoRequest> { HandleUserRepoRequest() }
    single<HandleMainRequest> { HandleMainRequest() }
    single<GetPagedReposUseCase> {
        GetPagedReposUseCaseImpl(
            handleMainRequest = get(),
            repository = get(),
            handleUserRepoRequest = get()
        )
    }
    viewModel {
        MainViewModel(
            getPagedReposUseCase = get(),
            mainUiMapper = get(),
            runAsync = get(),
            savedStateHandle = get(),
        )
    }
}

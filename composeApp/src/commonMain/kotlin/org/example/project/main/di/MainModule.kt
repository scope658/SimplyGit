package org.example.project.main.di

import org.example.project.core.data.cache.DataStoreManager
import org.example.project.core.data.cache.db.AppDatabase
import org.example.project.main.data.MainRepositoryImpl
import org.example.project.main.data.RepoData
import org.example.project.main.data.cache.RepoCache
import org.example.project.main.data.cache.RepoCacheToDomain
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.data.mappers.RepoDataToCache
import org.example.project.main.data.mappers.RepoDataToDomain
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.HandleMainRequest
import org.example.project.main.domain.HandleUserRepoRequest
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.PaginationResult
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.MainViewModel
import org.example.project.main.presentation.PagingUiState
import org.example.project.main.presentation.UserRepositoryUi
import org.example.project.main.presentation.mappers.MainUiMapper
import org.example.project.main.presentation.mappers.PagingUiStateMapper
import org.example.project.main.presentation.mappers.UserRepoToUiMapper
import org.example.project.main.presentation.mappers.UserRepoUiToDomain
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single<UserRepoDao> { get<AppDatabase>().userRepoDao() }
    single<GithubApi> { GithubApiImpl(get()) }
    single<DataStoreManager.ReadToken> { DataStoreManager.Base(dataStore = get()) }
    factory<RepoCache.Mapper<UserRepository>>(named("cacheToDomain")) { RepoCacheToDomain() }
    factory<RepoData.Mapper<RepoCache>>(named("RepoDataToCache")) { RepoDataToCache() }
    factory<RepoData.Mapper<UserRepository>>(named("RepoDataToDomain")) { RepoDataToDomain() }
    single<MainRepository> {
        MainRepositoryImpl(
            githubApi = get(),
            dao = get(),
            repoCacheToDomain = get(named("cacheToDomain")),
            repoDataToCache = get(named("RepoDataToCache")),
            repoDataToDomain = get(named("RepoDataToDomain")),
            customRunCatching = get(),
            handleDomainError = get(),
        )
    }
    factory<PaginationResult.Mapper<PagingUiState>> { PagingUiStateMapper() }
    factory<UserRepository.Mapper<UserRepositoryUi>> { UserRepoToUiMapper() }
    single<PagedResult.Mapper<MainUiState>> {
        MainUiMapper(
            pagingUiStateMapper = get(),
            userRepoToUiMapper = get(),
        )
    }
    single<HandleMainRequest> {
        HandleMainRequest.Base(
            manageResource = get()
        )
    }
    factory<HandleUserRepoRequest> {
        HandleUserRepoRequest.Base(
            manageResource = get()
        )
    }
    factory<UserRepositoryUi.Mapper<UserRepository>> { UserRepoUiToDomain() }
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
            userRepoUiToDomain = get(),
        )
    }
}

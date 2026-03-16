package org.example.project.profile.di

import org.example.project.core.cache.DataStoreManager
import org.example.project.core.cache.db.AppDatabase
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.profile.data.ProfileRepositoryImpl
import org.example.project.profile.data.cache.ProfileDao
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.domain.ProfileRepository
import org.example.project.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val profileModule = module {
    single<ProfileDao> { get<AppDatabase>().profileDao() }
    single<DataStoreManager.TokenOperations> { DataStoreManager.Base(get()) }
    single<ProfileGithubApi> { ProfileGithubApi.Base(httpClient = get()) }
    single<UserRepoDao.ClearAll> { get<AppDatabase>().userRepoDao() }
    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            profileDao = get(),
            githubApi = get(),
            dataStoreManager = get(),
            userReposDao = get()
        )
    }
    viewModel {
        ProfileViewModel(
            runAsync = get(),
            profileRepository = get()
        )
    }
}

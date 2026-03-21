package org.example.project.profile.di

import org.example.project.core.cache.db.AppDatabase
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.profile.data.ProfileData
import org.example.project.profile.data.ProfileRepositoryImpl
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileCacheToDomain
import org.example.project.profile.data.cache.ProfileDao
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.data.mappers.ProfileDataToCache
import org.example.project.profile.data.mappers.ProfileDataToDomain
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository
import org.example.project.profile.presentation.ProfileUiMapper
import org.example.project.profile.presentation.ProfileUiState
import org.example.project.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val profileModule = module {
    single<ProfileDao> { get<AppDatabase>().profileDao() }
    single<ProfileGithubApi> { ProfileGithubApi.Base(httpClient = get()) }
    single<UserRepoDao.ClearAll> { get<AppDatabase>().userRepoDao() }
    factory<ProfileData.Mapper<Profile>>(named("DataToDomain")) { ProfileDataToDomain() }
    factory<ProfileData.Mapper<ProfileCache>>(named("DataToCache")) { ProfileDataToCache() }
    factory<ProfileCache.Mapper<Profile>>(named("cacheToDomain")) { ProfileCacheToDomain() }
    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            profileDao = get(),
            githubApi = get(),
            dataStoreManager = get(),
            userReposDao = get(),
            profileDataToDomain = get(named("DataToDomain")),
            profileDataToCache = get(named("DataToCache")),
            profileCacheToDomain = get(named("cacheToDomain")),
        )
    }

    factory<Profile.Mapper<ProfileUiState>> { ProfileUiMapper() }
    viewModel {
        ProfileViewModel(
            runAsync = get(),
            profileRepository = get(),
            profileUiMapper = get()
        )
    }
}

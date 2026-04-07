package org.example.project.details.di

import org.example.project.core.data.cache.db.AppDatabase
import org.example.project.details.data.DetailsData
import org.example.project.details.data.DetailsDataToDomain
import org.example.project.details.data.DetailsRepositoryImpl
import org.example.project.details.data.ReadmeData
import org.example.project.details.data.cache.DetailsCacheDataSource
import org.example.project.details.data.cache.DetailsCacheDataSourceImpl
import org.example.project.details.data.cache.details.DetailsCache
import org.example.project.details.data.cache.details.DetailsCacheToData
import org.example.project.details.data.cache.details.DetailsDao
import org.example.project.details.data.cache.details.DetailsDataToCache
import org.example.project.details.data.cache.readme.ReadmeCache
import org.example.project.details.data.cache.readme.ReadmeDao
import org.example.project.details.data.cache.readme.ReadmeDataToCache
import org.example.project.details.data.cloud.DetailsGithubApi
import org.example.project.details.data.cloud.DetailsGithubApiImpl
import org.example.project.details.domain.CombinedDetailsResult
import org.example.project.details.domain.DetailsRepository
import org.example.project.details.domain.HandleDetailsRequest
import org.example.project.details.domain.RepoDetails
import org.example.project.details.domain.RepoDetailsUseCase
import org.example.project.details.domain.RepoDetailsUseCaseImpl
import org.example.project.details.presentation.DetailsUiMapper
import org.example.project.details.presentation.DetailsUiState
import org.example.project.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val detailsModule = module {
    single<DetailsDao> { get<AppDatabase>().detailsDao() }
    single<ReadmeDao> { get<AppDatabase>().readmeDao() }

    factory<DetailsCache.Mapper<DetailsData>> { DetailsCacheToData() }
    factory<DetailsData.Mapper<DetailsCache>>(named("dataToCache")) { DetailsDataToCache() }
    factory<ReadmeData.Mapper<ReadmeCache>> { ReadmeDataToCache() }

    factory<DetailsCacheDataSource> {
        DetailsCacheDataSourceImpl(
            detailsDao = get(),
            readmeDao = get(),
            detailsCacheToData = get(),
            detailsDataToCache = get(named("dataToCache")),
            readmeDataToCache = get()
        )
    }
    factory<DetailsGithubApi> { DetailsGithubApiImpl(get()) }
    factory<DetailsData.Mapper<RepoDetails>>(named("dataToDomain")) { DetailsDataToDomain() }
    factory<DetailsRepository> {
        DetailsRepositoryImpl(
            detailsCacheDataSource = get(),
            detailsDataToDomain = get(named("dataToDomain")),
            detailsGithubApi = get(),
            handleDomainError = get()
        )
    }

    factory<HandleDetailsRequest> { HandleDetailsRequest(get()) }
    factory<RepoDetailsUseCase> {
        RepoDetailsUseCaseImpl(
            handleDetailsRequest = get(),
            detailsRepository = get()
        )
    }


    factory<CombinedDetailsResult.Mapper<DetailsUiState>> { DetailsUiMapper() }

    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            runAsync = get(),
            detailsUiMapper = get(),
            repoDetailsUseCase = get(),
        )
    }
}

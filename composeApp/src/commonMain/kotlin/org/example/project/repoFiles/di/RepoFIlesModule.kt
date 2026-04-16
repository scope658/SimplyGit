package org.example.project.repoFiles.di

import org.example.project.core.presentation.RouteArgs
import org.example.project.repoFiles.data.FileTypeData
import org.example.project.repoFiles.data.FileTypeMapper
import org.example.project.repoFiles.data.RepoFilesData
import org.example.project.repoFiles.data.RepoFilesDataToDomain
import org.example.project.repoFiles.data.RepoFilesRepositoryImpl
import org.example.project.repoFiles.data.cloud.RepoFilesGithubApi
import org.example.project.repoFiles.data.cloud.RepoFilesGithubApiImpl
import org.example.project.repoFiles.domain.FileType
import org.example.project.repoFiles.domain.RepoFiles
import org.example.project.repoFiles.domain.RepoFilesRepository
import org.example.project.repoFiles.presentation.RepoFileUi
import org.example.project.repoFiles.presentation.RepoFilesArgs
import org.example.project.repoFiles.presentation.RepoFilesUiMapper
import org.example.project.repoFiles.presentation.RepoFilesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repoFilesModule = module {
    factory<RepoFilesGithubApi> {
        RepoFilesGithubApiImpl(
            httpClient = get()
        )
    }

    factory<FileTypeData.Mapper<FileType>> { FileTypeMapper() }
    factory<RepoFilesData.Mapper<RepoFiles>> {
        RepoFilesDataToDomain(
            fileTypeMapper = get()
        )
    }
    factory<RepoFilesRepository> {
        RepoFilesRepositoryImpl(
            repoFilesGithubApi = get(),
            repoFilesDataToDomain = get(),
            runCatchingSuspend = get()
        )
    }

    factory<RouteArgs.All> { RepoFilesArgs(get()) }
    factory<RepoFiles.Mapper<RepoFileUi>> { RepoFilesUiMapper() }
    viewModel {
        RepoFilesViewModel(
            runAsync = get(),
            repoFilesRepository = get(),
            savedStateHandle = get(),
            repoFilesArgs = get(),
            mapper = get(),
            manageResource = get(),
        )
    }
}

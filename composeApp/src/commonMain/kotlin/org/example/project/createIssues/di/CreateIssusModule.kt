package org.example.project.createIssues.di

import org.example.project.core.presentation.RouteArgs
import org.example.project.createIssues.data.IssueRepositoryImpl
import org.example.project.createIssues.data.cloud.IssueGithubApi
import org.example.project.createIssues.data.cloud.IssueGithubApiImpl
import org.example.project.createIssues.domain.CreateIssueUseCase
import org.example.project.createIssues.domain.CreateIssueUseCaseImpl
import org.example.project.createIssues.domain.IssueRepository
import org.example.project.createIssues.domain.IssueResult
import org.example.project.createIssues.domain.IssueValidator
import org.example.project.createIssues.domain.IssuesManageResource
import org.example.project.createIssues.presentation.IssueScreenState
import org.example.project.createIssues.presentation.IssuesArgs
import org.example.project.createIssues.presentation.IssuesManageResourceImpl
import org.example.project.createIssues.presentation.IssuesUiStateMapper
import org.example.project.createIssues.presentation.IssuesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createIssuesModule = module {

    factory<IssueGithubApi> {
        IssueGithubApiImpl(
            httpClient = get()
        )
    }
    factory<IssueRepository> {
        IssueRepositoryImpl(
            runCatchingSuspend = get(),
            issuesGithubApi = get(),
        )
    }

    factory<IssuesManageResource> { IssuesManageResourceImpl() }
    factory<IssueValidator> {
        IssueValidator(
            resource = get()
        )
    }

    factory<CreateIssueUseCase> {
        CreateIssueUseCaseImpl(
            manageResource = get(),
            issueValidator = get(),
            issueRepository = get()
        )
    }

    factory<IssueResult.Mapper<IssueScreenState>> { IssuesUiStateMapper() }


    factory<RouteArgs> { IssuesArgs(savedStateHandle = get()) }
    viewModel {
        IssuesViewModel(
            savedStateHandle = get(),
            runAsync = get(),
            createIssueUseCase = get(),
            issuesUiStateMapper = get(),
            issueArgs = get()
        )
    }

}

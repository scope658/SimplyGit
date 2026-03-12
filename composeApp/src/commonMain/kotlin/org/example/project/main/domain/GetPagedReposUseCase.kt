package org.example.project.main.domain

import io.github.aakira.napier.Napier

interface GetPagedReposUseCase {
    suspend fun userRepo(
        currentRepoList: List<UserRepository>,
        page: Int
    ): PagedResult

    suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int,
    ): PagedResult
}


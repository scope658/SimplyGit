package org.example.project.main.domain

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


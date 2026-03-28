package org.example.project.main.domain


interface GetPagedReposUseCase {
    suspend fun allUserRepos(): PagedResult

    suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int,
    ): PagedResult

    suspend fun refresh(): PagedResult
}


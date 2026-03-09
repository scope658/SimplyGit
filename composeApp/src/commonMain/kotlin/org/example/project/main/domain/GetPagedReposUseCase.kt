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

    class Base(
        private val repository: MainRepository,
        private val handleMainRequest: HandleMainRequest
    ) : GetPagedReposUseCase {
        override suspend fun userRepo(
            currentRepoList: List<UserRepository>,
            page: Int
        ): PagedResult {
            return handleMainRequest.handle(page, currentRepoList) {
                repository.userRepo(page)
            }
        }

        override suspend fun searchByQuery(
            currentRepoList: List<UserRepository>,
            userQuery: String,
            page: Int
        ): PagedResult {
            return handleMainRequest.handle(page, currentRepoList) {
                Napier.d("handler is pinged", tag = "dd21")
                repository.searchByQuery(
                    userQuery, page
                )
            }
        }

    }
}


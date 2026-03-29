package org.example.project.main.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException


class GetPagedReposUseCaseImpl(
    private val repository: MainRepository,
    private val handleUserRepoRequest: HandleUserRepoRequest,
    private val manageResource: ManageResource,
) : GetPagedReposUseCase {
    override suspend fun allUserRepos(): PagedResult {
        return handleUserRepoRequest.handle {
            repository.userRepo()
        }
    }

    override suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int
    ): PagedResult {
        return repository.searchByQuery(userQuery, page).fold(
            onSuccess = { userRepositories ->
                val totalRepos = currentRepoList + userRepositories
                val pagingState = if (userRepositories.size == DEFAULT_PAGE_SIZE) {
                    PaginationResult.ReadyForNext
                } else {
                    PaginationResult.ReachedBottom
                }

                if (page == 1 && userRepositories.isEmpty()) {
                    PagedResult.EmptyResult
                } else {
                    PagedResult.Success(
                        page = page + 1,
                        repos = totalRepos,
                        paginationResult = pagingState
                    )
                }
            },
            onFailure = {
                val error = it as? DomainException ?: ServiceUnavailableException
                val exceptionMessage = error.exceptionString(manageResource)
                if (page == FIRST_RUN_PAGE_SIZE) {
                    PagedResult.Failure(message = exceptionMessage)
                } else {
                    PagedResult.Success(
                        page = page,
                        repos = currentRepoList,
                        paginationResult = PaginationResult.Failure(message = exceptionMessage)
                    )
                }
            }
        )
    }

    override suspend fun refresh(): PagedResult {
        return handleUserRepoRequest.handle {
            repository.refresh()
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 15
        private const val FIRST_RUN_PAGE_SIZE = 1
    }
}

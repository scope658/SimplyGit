package org.example.project.main.domain

class GetPagedReposUseCaseImpl(
    private val repository: MainRepository,
    private val handleMainRequest: HandleMainRequest,
    private val handleUserRepoRequest: HandleUserRepoRequest,
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
        return handleMainRequest.handle(page, currentRepoList) {
            repository.searchByQuery(
                userQuery, page
            )
        }
    }

    override suspend fun refresh(): PagedResult {
        return handleUserRepoRequest.handle {
            repository.refresh()
        }
    }
}

package org.example.project.main.domain

class GetPagedReposUseCaseImpl(
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
            repository.searchByQuery(
                userQuery, page
            )
        }
    }
}

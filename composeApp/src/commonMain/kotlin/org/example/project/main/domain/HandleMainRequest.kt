package org.example.project.main.domain


class HandleMainRequest(private val manageResource: ManageResource) {
    suspend fun handle(
        page: Int,
        currentRepositories: List<UserRepository>,
        block: suspend () -> Result<List<UserRepository>>
    ): PagedResult {
        return block.invoke().fold(
            onSuccess = { userRepositories ->
                val totalRepos = currentRepositories + userRepositories
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
                        repos = currentRepositories,
                        paginationResult = PaginationResult.Failure(message = exceptionMessage)
                    )
                }
            }
        )
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 15
        private const val FIRST_RUN_PAGE_SIZE = 1
        private const val HARDCODED_FAILURE = "something went wrong"
    }
}

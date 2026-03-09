package org.example.project.main.domain

interface HandleMainRequest {

    suspend fun handle(
        page: Int,
        currentRepositories: List<UserRepository>,
        block: suspend () -> Result<List<UserRepository>>,
    ): PagedResult

    class Base : HandleMainRequest {

        override suspend fun handle(
            page: Int,
            currentRepositories: List<UserRepository>,
            block: suspend () -> Result<List<UserRepository>>
        ): PagedResult {
            return block.invoke().fold(
                onSuccess = { userRepositories ->
                    val totalRepos = currentRepositories + userRepositories
                    val isLoadMore = userRepositories.size == DEFAULT_PAGE_SIZE
                    if (page == 1 && userRepositories.isEmpty()) {
                        PagedResult.EmptyResult
                    } else {
                        PagedResult.Success(
                            page = page,
                            isPagingException = false,
                            isLoadMore = isLoadMore,
                            repos = totalRepos
                        )
                    }
                },
                onFailure = {

                    if (page == FIRST_RUN_PAGE_SIZE) {
                        PagedResult.Failure(it.message ?: HARDCODED_FAILURE)
                    } else {
                        PagedResult.Success(
                            page,
                            isPagingException = true,
                            isLoadMore = true,
                            currentRepositories
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
}

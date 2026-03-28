package org.example.project.main.domain


class HandleUserRepoRequest {
    suspend fun handle(block: suspend () -> Result<List<UserRepository>>): PagedResult {
        block.invoke().fold(
            onSuccess = { repos ->
                return if (repos.isEmpty()) {
                    PagedResult.EmptyResult
                } else {
                    PagedResult.Success(
                        page = 0,
                        isPagingException = false,
                        isLoadMore = false,
                        repos = repos,
                    )
                }
            },
            onFailure = {
                return PagedResult.Failure(it.message.orEmpty())
            }
        )
    }
}


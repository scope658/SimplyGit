package org.example.project.main.domain

interface HandleUserRepoRequest {

    suspend fun handle(block: suspend () -> Result<List<UserRepository>>): PagedResult

    class Base : HandleUserRepoRequest {
        override suspend fun handle(block: suspend () -> Result<List<UserRepository>>): PagedResult {
            block.invoke().fold(
                onSuccess = { repos ->
                    return if (repos.isEmpty()) {
                        PagedResult.EmptyResult
                    } else {
                        PagedResult.Success(
                            page = 0,
                            repos = repos,
                            paginationResult = PaginationResult.ReachedBottom
                        )
                    }
                },
                onFailure = {
                    return PagedResult.Failure(it.message.orEmpty())
                }
            )
        }
    }
}

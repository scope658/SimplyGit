package org.example.project.main.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException


class HandleUserRepoRequest(private val manageResource: ManageResource) {
    suspend fun handle(block: suspend () -> Result<List<UserRepository>>): PagedResult {
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
                val error = it as? DomainException ?: ServiceUnavailableException
                val exceptionMessage = error.exceptionString(manageResource)
                return PagedResult.Failure(exceptionMessage)
            }
        )
    }
}


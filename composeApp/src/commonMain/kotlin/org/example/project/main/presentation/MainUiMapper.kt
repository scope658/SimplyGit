package org.example.project.main.presentation

import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.UserRepository

class MainUiMapper : PagedResult.Mapper {

    override fun mapSuccess(
        page: Int,
        isPagingException: Boolean,
        isLoadMore: Boolean,
        repos: List<UserRepository>
    ): MainUiState {
        val currentPagingState = when {
            isPagingException -> PagingUiState.Failure(HARDCODED_FAILURE)
            !isLoadMore -> PagingUiState.Empty
            else -> PagingUiState.Loading
        }
        return MainUiState.Success(
            page = page,
            isLoadMore = isLoadMore,
            pagingUiState = currentPagingState,
            result = repos.toUi(),
        )
    }

    override fun mapFailure(message: String): MainUiState {
        return MainUiState.Failure(message)
    }

    override fun emptyResult(): MainUiState {
        return MainUiState.EmptyResult
    }

    companion object {
        private const val HARDCODED_FAILURE = "something went wrong"
    }
}

fun List<UserRepository>.toUi() = this.map {
    UserRepositoryUi(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}


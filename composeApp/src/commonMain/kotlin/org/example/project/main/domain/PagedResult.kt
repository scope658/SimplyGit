package org.example.project.main.domain

import org.example.project.main.presentation.MainUiState

interface PagedResult {

    fun map(mapper: Mapper): MainUiState

    interface Mapper {
        fun mapSuccess(
            page: Int,
            isPagingException: Boolean,
            isLoadMore: Boolean,
            repos: List<UserRepository>
        ): MainUiState

        fun mapFailure(message: String): MainUiState
        fun emptyResult(): MainUiState
    }


    data class Success(
        val page: Int,
        val isPagingException: Boolean,
        val isLoadMore: Boolean,
        val repos: List<UserRepository>,
    ) : PagedResult {
        override fun map(mapper: Mapper): MainUiState {
            return mapper.mapSuccess(page = page, isPagingException, isLoadMore, repos)
        }
    }

    data class Failure(
        val message: String,
    ) : PagedResult {
        override fun map(mapper: Mapper): MainUiState {
            return mapper.mapFailure(message)
        }
    }

    object EmptyResult : PagedResult {
        override fun map(mapper: Mapper): MainUiState {
            return mapper.emptyResult()
        }
    }
}

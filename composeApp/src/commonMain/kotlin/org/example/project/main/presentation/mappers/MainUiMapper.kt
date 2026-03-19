package org.example.project.main.presentation.mappers

import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.PaginationResult
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.PagingUiState
import org.example.project.main.presentation.UserRepositoryUi

class MainUiMapper(
    private val pagingUiStateMapper: PaginationResult.Mapper<PagingUiState>,
    private val userRepoToUiMapper: UserRepository.Mapper<UserRepositoryUi>
) :
    PagedResult.Mapper<MainUiState> {

    override fun mapSuccess(
        page: Int,
        repos: List<UserRepository>,
        paginationResult: PaginationResult,
    ): MainUiState {

        return MainUiState.Success(
            page = page,
            result = repos.map { it.map(mapper = userRepoToUiMapper) },
            pagingUiState = paginationResult.map(mapper = pagingUiStateMapper)
        )
    }

    override fun mapFailure(message: String): MainUiState {
        return MainUiState.Failure(message)
    }

    override fun emptyResult(): MainUiState {
        return MainUiState.EmptyResult
    }
}

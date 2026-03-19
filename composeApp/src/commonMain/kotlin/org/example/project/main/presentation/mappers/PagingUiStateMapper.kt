package org.example.project.main.presentation.mappers

import org.example.project.main.domain.PaginationResult
import org.example.project.main.presentation.PagingUiState

class PagingUiStateMapper : PaginationResult.Mapper<PagingUiState> {
    override fun mapReadyForNext(): PagingUiState {
        return PagingUiState.Loading
    }

    override fun mapReachedBottom(): PagingUiState {
        return PagingUiState.Empty
    }

    override fun mapFailure(message: String): PagingUiState {
        return PagingUiState.Failure(message)
    }

}

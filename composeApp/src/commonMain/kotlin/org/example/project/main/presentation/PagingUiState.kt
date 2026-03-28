package org.example.project.main.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface PagingUiState {

    @Serializable
    data class Failure(val message: String) : PagingUiState

    @Serializable
    object Loading : PagingUiState

    @Serializable
    object Empty : PagingUiState
}

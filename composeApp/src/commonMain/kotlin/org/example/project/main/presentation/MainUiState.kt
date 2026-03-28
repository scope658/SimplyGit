package org.example.project.main.presentation

import kotlinx.serialization.Serializable


data class MainScreenState(
    val isRefreshing: Boolean,
    val mainUiState: MainUiState,
)
@Serializable
sealed interface MainUiState {

    @Serializable
    data class Success(
        val page: Int,
        val pagingUiState: PagingUiState,
        val result: List<UserRepositoryUi>
    ) : MainUiState

    @Serializable
    object Loading : MainUiState

    @Serializable
    object EmptyResult : MainUiState

    @Serializable
    data class Failure(val message: String) : MainUiState

}

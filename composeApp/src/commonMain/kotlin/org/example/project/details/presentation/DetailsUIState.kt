package org.example.project.details.presentation


data class DetailsScreenState(
    val isRefreshing: Boolean,
    val detailsUiState: DetailsUiState,
)

sealed interface DetailsUiState {
    data class Success(
        val repoOwner: String,
        val repoName: String,
        val repoDesc: String,
        val forksCount: String,
        val issuesCount: String,
        val programmingLanguage: String,
        val readme: ReadmeUiState,
    ) : DetailsUiState

    object Loading : DetailsUiState

    data class Failure(
        val message: String
    ) : DetailsUiState
}

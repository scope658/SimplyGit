package org.example.project.details.presentation

sealed interface ReadmeUiState {
    data class Success(
        val readme: String,
    ) : ReadmeUiState
}

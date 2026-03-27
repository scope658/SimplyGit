package org.example.project.login.presentation


import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginUiState {

    @Serializable
    data class Initial(val errorState: ErrorState) : LoginUiState

    @Serializable
    object Loading : LoginUiState
}

@Serializable
sealed interface ErrorState {

    @Serializable
    object Empty : ErrorState

    @Serializable
    data class Error(val message: String) : ErrorState
}

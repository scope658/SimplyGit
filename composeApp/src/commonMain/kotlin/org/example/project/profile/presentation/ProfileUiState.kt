package org.example.project.profile.presentation

import org.example.project.core.AbstractScreen

interface ProfileUiState {

    data class Success(
        val avatar: String,
        val userName: String,
        val bio: String,
        val repoCount: String,
        val subscribersCount: String,
    ) : ProfileUiState

    data class Failure(
        val message: String,
    ) : ProfileUiState, AbstractScreen()

    object Loading : ProfileUiState, AbstractScreen()

}

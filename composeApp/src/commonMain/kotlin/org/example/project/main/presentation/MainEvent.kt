package org.example.project.main.presentation

sealed class MainEvent {
    data class OnDetails(
        val repoOwner: String,
        val repoName: String,
    ) : MainEvent()
}

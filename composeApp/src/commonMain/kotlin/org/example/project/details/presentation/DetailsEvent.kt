package org.example.project.details.presentation


sealed interface DetailsEvent {
    data class OnCode(
        val repoOwner: String,
        val repoName: String,
    ) : DetailsEvent

    data class OnCreateIssues(
        val repoOwner: String,
        val repoName: String,
    ) : DetailsEvent
}

package org.example.project.repoFiles.presentation

sealed interface RepoFilesEvent {
    data class OpenDirectory(
        val repoOwner: String,
        val repoName: String,
        val path: String
    ) : RepoFilesEvent

    data class OnFileDetails(
        val repoOwner: String,
        val repoName: String,
        val path: String
    ) : RepoFilesEvent
}

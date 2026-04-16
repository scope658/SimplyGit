package org.example.project.repoFiles.presentation

import kotlinx.serialization.Serializable


@Serializable
sealed interface RepoFilesUiState {

    @Serializable
    data class Success(
        val repoFiles: List<RepoFileUi>
    ) : RepoFilesUiState

    @Serializable
    data class Failure(val message: String) : RepoFilesUiState

    @Serializable
    object Loading : RepoFilesUiState
}

@Serializable
sealed interface FileTypeUi {

    @Serializable
    object File : FileTypeUi

    @Serializable
    object Dir : FileTypeUi

    @Serializable
    object SymLink : FileTypeUi
}

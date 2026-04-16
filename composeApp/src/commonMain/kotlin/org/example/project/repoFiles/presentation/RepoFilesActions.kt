package org.example.project.repoFiles.presentation

interface RepoFilesActions {
    fun onFileDetails(downloadUrl: String)
    fun openDirectory(targetPath: String)
    fun retry()
}

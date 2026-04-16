package org.example.project.repoFiles.domain

interface RepoFilesRepository {
    suspend fun repoFiles(
        repoOwner: String,
        repoName: String,
        path: String
    ): Result<List<RepoFiles>>
}

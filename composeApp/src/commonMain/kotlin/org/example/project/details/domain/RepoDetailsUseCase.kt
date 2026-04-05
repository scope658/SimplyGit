package org.example.project.details.domain

interface RepoDetailsUseCase {
    suspend fun repoDetails(repoOwner: String, repoName: String): CombinedDetailsResult
    suspend fun refreshRepoDetails(repoOwner: String, repoName: String): CombinedDetailsResult
}


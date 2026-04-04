package org.example.project.details.domain

interface DetailsRepository {

    suspend fun repoDetails(repoOwner: String, repoName: String): Result<RepoDetails>
    suspend fun readme(repoOwner: String, repoName: String): Result<String>
    suspend fun refreshReadme(repoOwner: String, repoName: String): Result<String>
    suspend fun refreshDetails(repoOwner: String, repoName: String): Result<RepoDetails>
}

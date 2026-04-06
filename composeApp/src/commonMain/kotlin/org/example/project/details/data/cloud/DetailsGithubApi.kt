package org.example.project.details.data.cloud

import org.example.project.details.data.DetailsData
import org.example.project.details.data.ReadmeData

interface DetailsGithubApi {
    suspend fun details(repoOwner: String, repoName: String): DetailsData
    suspend fun readme(repoOwner: String, repoName: String): ReadmeData
}

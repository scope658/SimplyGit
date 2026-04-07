package org.example.project.details.data.cloud

import org.example.project.details.data.DetailsData
import org.example.project.details.data.ReadmeData

interface DetailsGithubApi {
    suspend fun details(repoOwner: String, repoName: String): DetailsData
    suspend fun readme(repoOwner: String, repoName: String): ReadmeData
}

class FakeDetailsGithubApi : DetailsGithubApi {
    private var isDetailsFailure = false
    private lateinit var exception: Exception
    override suspend fun details(
        repoOwner: String,
        repoName: String
    ): DetailsData {
        if (isDetailsFailure) {
            throw exception
        } else {
            return DetailsData(
                repoOwner = "repo owner",
                repoName = "repo name",
                repoDesc = "repo desc",
                forksCount = 0,
                issuesCount = 0,
                programmingLanguage = "kotlin",
            )
        }
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): ReadmeData {
        return ReadmeData(
            "repo owner",
            "repo name",
            "fake readme text"
        )
    }

    fun isDetailsFailure(flag: Boolean) {
        isDetailsFailure = flag
    }
}

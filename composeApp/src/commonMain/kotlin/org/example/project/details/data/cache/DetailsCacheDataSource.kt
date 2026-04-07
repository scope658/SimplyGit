package org.example.project.details.data.cache

import org.example.project.details.data.DetailsData
import org.example.project.details.data.ReadmeData

interface DetailsCacheDataSource {
    suspend fun isReadmeContains(repoOwner: String, repoName: String): Boolean
    suspend fun isDetailsContains(repoOwner: String, repoName: String): Boolean
    suspend fun details(repoOwner: String, repoName: String): DetailsData
    suspend fun readme(repoOwner: String, repoName: String): ReadmeData
    suspend fun saveReadme(readmeData: ReadmeData)
    suspend fun saveDetails(detailsData: DetailsData)
}

class FakeDetailsCacheDataSource : DetailsCacheDataSource {

    override suspend fun isReadmeContains(
        repoOwner: String,
        repoName: String
    ): Boolean = false

    override suspend fun isDetailsContains(
        repoOwner: String,
        repoName: String
    ): Boolean = false

    override suspend fun details(
        repoOwner: String,
        repoName: String
    ): DetailsData {
        return DetailsData(
            repoOwner = "scope",
            repoName = "repo name",
            repoDesc = "repo desc",
            forksCount = 0,
            issuesCount = 0,
            programmingLanguage = "kotlin",
        )
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): ReadmeData {
        return ReadmeData(
            repoOwner,
            repoName,
            "fake readme text"
        )
    }

    override suspend fun saveReadme(readmeData: ReadmeData) = Unit
    override suspend fun saveDetails(detailsData: DetailsData) = Unit
}

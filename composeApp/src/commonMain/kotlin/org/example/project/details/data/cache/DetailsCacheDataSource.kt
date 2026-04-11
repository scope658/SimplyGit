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

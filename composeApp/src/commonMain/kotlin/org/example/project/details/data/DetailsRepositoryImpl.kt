package org.example.project.details.data

import org.example.project.core.data.HandleDomainError
import org.example.project.details.data.cache.DetailsCacheDataSource
import org.example.project.details.data.cloud.DetailsGithubApi
import org.example.project.details.domain.DetailsRepository
import org.example.project.details.domain.RepoDetails

class DetailsRepositoryImpl(
    private val detailsCacheDataSource: DetailsCacheDataSource,
    private val detailsDataToDomain: DetailsData.Mapper<RepoDetails>,
    private val detailsGithubApi: DetailsGithubApi,
    private val handleDomainError: HandleDomainError,
) :
    DetailsRepository {
    override suspend fun repoDetails(
        repoOwner: String,
        repoName: String
    ): Result<RepoDetails> {
        return if (detailsCacheDataSource.isDetailsContains(repoOwner, repoName)) {
            val details = detailsCacheDataSource.details(repoOwner, repoName)
            return Result.success(details.map(mapper = detailsDataToDomain))
        } else {
            refreshDetails(repoOwner, repoName)
        }
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): Result<String> {
        return if (detailsCacheDataSource.isReadmeContains(repoOwner, repoName)) {
            val readme = detailsCacheDataSource.readme(repoOwner, repoName)
            return Result.success(readme.readme)
        } else {
            refreshReadme(repoOwner, repoName)
        }
    }

    override suspend fun refreshReadme(
        repoOwner: String,
        repoName: String
    ): Result<String> {
        return try {
            val readme = detailsGithubApi.readme(repoOwner, repoName)
            detailsCacheDataSource.saveReadme(readme)
            Result.success(readme.readme)
        } catch (e: Exception) {
            if (detailsCacheDataSource.isReadmeContains(repoOwner, repoName)) {
                val readme = detailsCacheDataSource.readme(repoOwner, repoName)
                Result.success(readme.readme)
            } else {
                val handledException = handleDomainError.handle(e)
                Result.failure(handledException)
            }
        }
    }

    override suspend fun refreshDetails(
        repoOwner: String,
        repoName: String
    ): Result<RepoDetails> {
        return try {
            val details = detailsGithubApi.details(repoOwner, repoName)
            detailsCacheDataSource.saveDetails(details)
            Result.success(details.map(mapper = detailsDataToDomain))
        } catch (e: Exception) {
            if (detailsCacheDataSource.isDetailsContains(repoOwner, repoName)) {
                val details = detailsCacheDataSource.details(repoOwner, repoName)
                Result.success(details.map(detailsDataToDomain))
            } else {
                val handledException = handleDomainError.handle(e)
                Result.failure(handledException)
            }
        }
    }
}

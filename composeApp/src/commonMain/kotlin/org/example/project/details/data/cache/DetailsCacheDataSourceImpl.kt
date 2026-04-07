package org.example.project.details.data.cache

import org.example.project.details.data.DetailsData
import org.example.project.details.data.ReadmeData
import org.example.project.details.data.cache.details.DetailsCache
import org.example.project.details.data.cache.details.DetailsDao
import org.example.project.details.data.cache.readme.ReadmeCache
import org.example.project.details.data.cache.readme.ReadmeDao

class DetailsCacheDataSourceImpl(
    private val detailsDao: DetailsDao,
    private val readmeDao: ReadmeDao,
    private val detailsCacheToData: DetailsCache.Mapper<DetailsData>,
    private val detailsDataToCache: DetailsData.Mapper<DetailsCache>,
    private val readmeDataToCache: ReadmeData.Mapper<ReadmeCache>,
) : DetailsCacheDataSource {
    override suspend fun isReadmeContains(
        repoOwner: String,
        repoName: String
    ): Boolean {
        return readmeDao.readme(repoOwner, repoName) != null
    }

    override suspend fun isDetailsContains(
        repoOwner: String,
        repoName: String
    ): Boolean {
        return detailsDao.details(repoOwner, repoName) != null
    }

    override suspend fun details(
        repoOwner: String,
        repoName: String
    ): DetailsData {
        val details: DetailsCache = detailsDao.details(repoOwner, repoName) ?: DetailsCache.EMPTY
        return details.map(detailsCacheToData)
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): ReadmeData {
        val readmeCache = readmeDao.readme(repoOwner, repoName) ?: ReadmeCache("", "", "")
        return ReadmeData(
            repoOwner = repoOwner,
            repoName = repoName,
            readme = readmeCache.readme
        )
    }

    override suspend fun saveReadme(readmeData: ReadmeData) {
        readmeDao.insert(readmeData.map(readmeDataToCache))
    }

    override suspend fun saveDetails(detailsData: DetailsData) {
        detailsDao.insert(detailsData.map(mapper = detailsDataToCache))
    }
}

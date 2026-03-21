package org.example.project.main.data.mappers

import org.example.project.main.data.RepoData
import org.example.project.main.data.cache.RepoCache

class RepoDataToCache : RepoData.Mapper<RepoCache> {
    override fun map(
        id: Int,
        userPhotoImageUrl: String,
        userName: String,
        repositoryName: String,
        programmingLanguage: String,
        stars: Int
    ): RepoCache {
        return RepoCache(
            id.toLong(),
            userPhotoImageUrl,
            userName,
            repositoryName,
            programmingLanguage,
            stars
        )
    }
}

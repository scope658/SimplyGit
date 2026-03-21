package org.example.project.main.data.cache

import org.example.project.main.domain.UserRepository

class RepoCacheToDomain : RepoCache.Mapper<UserRepository> {
    override fun map(
        id: Long,
        userPhotoUrl: String,
        userName: String,
        repoName: String,
        programmingLanguage: String,
        stars: Int
    ): UserRepository {
        return UserRepository(
            id.toInt(),
            userPhotoUrl,
            userName,
            repoName,
            programmingLanguage,
            stars
        )
    }
}

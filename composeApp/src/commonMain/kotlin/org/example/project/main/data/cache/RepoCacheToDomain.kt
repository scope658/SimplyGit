package org.example.project.main.data.cache

import org.example.project.main.domain.UserRepository

class RepoCacheToDomain : RepoCache.Mapper<UserRepository> {
    override fun map(
        repoCache: RepoCache
    ): UserRepository = with(repoCache) {
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

package org.example.project.main.data.mappers

import org.example.project.main.data.RepoData
import org.example.project.main.domain.UserRepository

class RepoDataToDomain : RepoData.Mapper<UserRepository> {
    override fun map(
        repoData: RepoData
    ): UserRepository = with(repoData) {
        return UserRepository(
            id = id,
            userPhotoImageUrl = userPhotoImageUrl,
            userName = userName,
            repositoryName = repositoryName,
            programmingLanguage = programmingLanguage,
            stars = stars
        )
    }
}

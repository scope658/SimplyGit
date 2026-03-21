package org.example.project.main.data.mappers

import org.example.project.main.data.RepoData
import org.example.project.main.domain.UserRepository

class RepoDataToDomain : RepoData.Mapper<UserRepository> {
    override fun map(
        id: Int,
        userPhotoImageUrl: String,
        userName: String,
        repositoryName: String,
        programmingLanguage: String,
        stars: Int
    ): UserRepository {
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

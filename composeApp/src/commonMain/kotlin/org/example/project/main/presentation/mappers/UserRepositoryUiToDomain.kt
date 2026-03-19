package org.example.project.main.presentation.mappers

import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.UserRepositoryUi

class UserRepoUiToDomain : UserRepositoryUi.Mapper<UserRepository> {
    override fun map(
        id: Int,
        userPhotoImageUrl: String,
        userName: String,
        repositoryName: String,
        programmingLanguage: String,
        stars: Int
    ): UserRepository {
        return UserRepository(
            id,
            userPhotoImageUrl,
            userName,
            repositoryName,
            programmingLanguage,
            stars
        )
    }

}

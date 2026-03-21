package org.example.project.main.presentation.mappers

import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.UserRepositoryUi

class UserRepoUiToDomain : UserRepositoryUi.Mapper<UserRepository> {
    override fun map(
        userRepositoryUi: UserRepositoryUi
    ): UserRepository = with(userRepositoryUi) {
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

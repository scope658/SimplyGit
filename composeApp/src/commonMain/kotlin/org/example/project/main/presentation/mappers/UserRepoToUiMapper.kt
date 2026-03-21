package org.example.project.main.presentation.mappers

import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.UserRepositoryUi

class UserRepoToUiMapper : UserRepository.Mapper<UserRepositoryUi> {
    override fun map(
        userRepository: UserRepository
    ): UserRepositoryUi = with(userRepository) {
        return UserRepositoryUi(
            id = id,
            userPhotoImageUrl = userPhotoImageUrl,
            userName = userName,
            repositoryName = repositoryName,
            programmingLanguage = programmingLanguage,
            stars = stars,
        )
    }
}

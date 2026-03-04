package org.example.project.main.domain

import org.example.project.main.presentation.UserRepositoryUi

data class UserRepository(
    val id: Int,
    val userPhotoImageUrl: String,
    val userName: String,
    val repositoryName: String,
    val programmingLanguage: String,
    val stars: Int,
)

fun List<UserRepository>.toUi() = this.map {
    UserRepositoryUi(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

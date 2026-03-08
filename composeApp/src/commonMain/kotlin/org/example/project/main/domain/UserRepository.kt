package org.example.project.main.domain


data class UserRepository(
    val id: Int,
    val userPhotoImageUrl: String,
    val userName: String,
    val repositoryName: String,
    val programmingLanguage: String,
    val stars: Int,
)

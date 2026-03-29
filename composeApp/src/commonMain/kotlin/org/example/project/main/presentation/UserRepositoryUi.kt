package org.example.project.main.presentation

import kotlinx.serialization.Serializable

@Serializable
data class UserRepositoryUi(
    val id: Int,
    val userPhotoImageUrl: String,
    val userName: String,
    val repositoryName: String,
    val programmingLanguage: String,
    val stars: Int,
) {
    interface Mapper<T> {
        fun map(
            userRepositoryUi: UserRepositoryUi
        ): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(
            this
        )
    }
}



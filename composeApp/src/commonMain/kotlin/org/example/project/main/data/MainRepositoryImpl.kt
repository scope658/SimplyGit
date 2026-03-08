package org.example.project.main.data

import org.example.project.core.customRunCatching
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.data.cloud.RepoData
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

class MainRepositoryImpl(private val githubApi: GithubApi) : MainRepository {

    override suspend fun userRepo(): Result<List<UserRepository>> {
        return customRunCatching {
            githubApi.userRepositories().toDomain()
        }
    }

    override suspend fun searchByQuery(userQuery: String): Result<List<UserRepository>> {
        return customRunCatching {
            githubApi.fetchByQuery(userQuery).toDomain()
        }
    }
}

fun List<RepoData>.toDomain() = this.map {
    UserRepository(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

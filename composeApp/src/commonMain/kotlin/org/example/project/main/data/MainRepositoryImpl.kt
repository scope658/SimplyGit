package org.example.project.main.data

import org.example.project.core.runCatchingSuspend
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.data.cloud.RepoData
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

class MainRepositoryImpl(private val githubApi: GithubApi) : MainRepository {

    override suspend fun userRepo(page: Int): Result<List<UserRepository>> {
        return runCatchingSuspend {
            githubApi.userRepositories(page).toDomain()
        }
    }

    override suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>> {
        return runCatchingSuspend {
            githubApi.fetchByQuery(userQuery, page).toDomain()
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

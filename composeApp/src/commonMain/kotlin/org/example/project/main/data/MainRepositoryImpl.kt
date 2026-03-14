package org.example.project.main.data

import org.example.project.core.cache.DataStoreManager
import org.example.project.core.customRunCatching
import org.example.project.core.runCatchingSuspend
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

class MainRepositoryImpl(
    private val githubApi: GithubApi,
    private val dataStoreManager: DataStoreManager.ReadToken
) : MainRepository {

    override suspend fun userRepo(page: Int): Result<List<UserRepository>> {
        val userToken = dataStoreManager.userToken() ?: ""
        return runCatchingSuspend {
            githubApi.userRepositories(page, userToken).toDomain()
        }
    }

    override suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>> {
        val userToken = dataStoreManager.userToken() ?: ""
        return runCatchingSuspend {
            githubApi.fetchByQuery(userQuery, page, userToken).toDomain()
        }
    }
}

fun List<RepoData>.toDomain() = this.map {
    UserRepository(
        id = it.id,
        userPhotageUrl = it.userPhotoImageUrl,
        userName oIm= it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

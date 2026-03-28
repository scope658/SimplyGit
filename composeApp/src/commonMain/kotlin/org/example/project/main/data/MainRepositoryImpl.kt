package org.example.project.main.data

import io.ktor.utils.io.CancellationException
import org.example.project.core.cache.DataStoreManager
import org.example.project.core.runCatchingSuspend
import org.example.project.main.data.cache.RepoCache
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

class MainRepositoryImpl(
    private val githubApi: GithubApi,
    private val dataStoreManager: DataStoreManager.ReadToken,
    private val dao: UserRepoDao
) : MainRepository {

    override suspend fun userRepo(): Result<List<UserRepository>> {
        val userRepos = dao.readUserRepos()
        return if (userRepos.isNotEmpty()) {
            Result.success(userRepos.toDomain())
        } else {
            refresh()
        }
    }

    override suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>> {
        val userToken = dataStoreManager.userToken() ?: ""
        return runCatchingSuspend {
            githubApi.fetchByQuery(userQuery, page, userToken).toDomainRepos()
        }
    }

    override suspend fun refresh(): Result<List<UserRepository>> {
        val userToken = dataStoreManager.userToken() ?: ""
        try {
            val repos = githubApi.userRepositories(userToken)
            dao.addUserRepos(repos.toCache())
            return Result.success(repos.toDomainRepos())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val userRepos = dao.readUserRepos()
            if (userRepos.isNotEmpty()) {
                return Result.success(userRepos.toDomain())
            } else {
                return Result.failure(Throwable(e.message))
            }
        }
    }
}

fun List<RepoData>.toCache() = this.map {
    RepoCache(
        id = it.id.toLong(),
        userPhotoUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repoName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

fun List<RepoData>.toDomainRepos() = this.map {
    UserRepository(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

fun List<RepoCache>.toDomain() = this.map {
    UserRepository(
        id = it.id.toInt(),
        userPhotoImageUrl = it.userPhotoUrl,
        userName = it.userName,
        repositoryName = it.repoName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

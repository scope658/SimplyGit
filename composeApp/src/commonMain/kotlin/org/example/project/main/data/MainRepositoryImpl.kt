package org.example.project.main.data

import io.ktor.utils.io.CancellationException
import org.example.project.core.customRunCatching
import org.example.project.main.data.cache.RepoCache
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

class MainRepositoryImpl(
    private val githubApi: GithubApi,
    private val dao: UserRepoDao,
    private val customRunCatching: CustomRunCatching,
    private val repoCacheToDomain: RepoCache.Mapper<UserRepository>,
    private val repoDataToCache: RepoData.Mapper<RepoCache>,
    private val repoDataToDomain: RepoData.Mapper<UserRepository>,
    private val handleDomainError: HandleDomainError,
) : MainRepository {

    override suspend fun userRepo(): Result<List<UserRepository>> {
        val userRepos = dao.readUserRepos()
        return if (userRepos.isNotEmpty()) {
            Result.success(userRepos.map { it.map(mapper = repoCacheToDomain) })
        } else {
            refresh()
        }
    }

    override suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>> {
        return customRunCatching.cath {
            githubApi.fetchByQuery(userQuery, page)
                .map { it.map(mapper = repoDataToDomain) }
        }
    }

    override suspend fun refresh(): Result<List<UserRepository>> {
        try {
            val repos = githubApi.userRepositories()
            dao.addUserRepos(repos.map { it.map(mapper = repoDataToCache) })
            return Result.success(repos.map { it.map(mapper = repoDataToDomain) })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val domainException = handleDomainError.handle(e)
            val userRepos = dao.readUserRepos()
            if (userRepos.isNotEmpty()) {
                return Result.success(userRepos.map { it.map(mapper = repoCacheToDomain) })
            } else {
                return Result.failure(domainException)
            }
        }
    }
}

package org.example.project.main.data

import kotlinx.coroutines.runBlocking
import org.example.project.MockData
import org.example.project.core.CustomRunCatching
import org.example.project.core.HandleDomainError
import org.example.project.core.cloud.FakeGithubApi
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.main.data.cache.RepoCache
import org.example.project.main.data.cache.RepoCacheToDomain
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.main.data.mappers.RepoDataToCache
import org.example.project.main.data.mappers.RepoDataToDomain
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainRepositoryTest {
    private lateinit var mainRepository: MainRepository
    private lateinit var fakeGithubApi: FakeGithubApi
    private lateinit var fakeDao: FakeDao
    @BeforeTest
    fun setUp() {
        fakeDao = FakeDao()
        fakeGithubApi = FakeGithubApi()
        val repoCacheToDomain: RepoCache.Mapper<UserRepository> = RepoCacheToDomain()
        val repoDataToCache: RepoData.Mapper<RepoCache> = RepoDataToCache()
        val repoDataToDomain: RepoData.Mapper<UserRepository> = RepoDataToDomain()
        val customRunCatching = CustomRunCatching.Base(handleDomainError = HandleDomainError.Base())
        mainRepository =
            MainRepositoryImpl(
                githubApi = fakeGithubApi,
                dao = fakeDao,
                repoCacheToDomain = repoCacheToDomain,
                repoDataToCache = repoDataToCache,
                repoDataToDomain = repoDataToDomain,
                customRunCatching = customRunCatching,
                handleDomainError = HandleDomainError.Base(),
            )
    }

    @Test
    fun `success query`() = runBlocking {

        val expectedResult = Result.success(MockData.mockedSearchResults)
        val actualResult = mainRepository.searchByQuery(FAKE_QUERY, 1)

        assertEquals(expectedResult, actualResult)

    }

    @Test
    fun `success user repositories then cache`() = runBlocking {
        fakeGithubApi.setException(null)
        val actualResult = mainRepository.userRepo()
        val expectedResult = Result.success(MockData.mockedUserDataRepositories.toDomainRepos())

        assertEquals(expectedResult, actualResult)
        fakeDao.checkAddIsCalled(MockData.mockedUserDataRepositories.toCache())
    }

    @Test
    fun `failure query`() = runBlocking {
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.searchByQuery(FAKE_QUERY, 1)
        actualResult
            .onFailure {
                val error = it as DomainException
                assertEquals(ServiceUnavailableException, error)
            }
        Unit
    }

    @Test
    fun `failure user repositories no cache`() = runBlocking { //user repo trigger refresh
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        mainRepository.userRepo()
            .onFailure {
                val error = it as DomainException
                assertEquals(ServiceUnavailableException, error)
            }
        Unit
    }

    @Test
    fun `success refresh`() = runBlocking {
        fakeGithubApi.setException(null)

        val actualResult = mainRepository.refresh()
        val expectedResult = Result.success(MockData.mockedUserDataRepositories.toDomainRepos())

        assertEquals(actualResult, expectedResult)
        fakeDao.checkAddIsCalled(MockData.mockedUserDataRepositories.toCache())
    }

    @Test
    fun `failure but cache contain`() = runBlocking {
        fakeDao.addUserRepos(expectedListUserRepos)
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.userRepo()
        val expectedResult = Result.success(expectedListUserRepos.toDomain())

        assertEquals(expectedResult, actualResult)
    }

    companion object {
        private const val FAKE_QUERY = "fake query"
        private const val FAKE_EXCEPTION_MESSAGE = "fake message"
    }
}

private val expectedListUserRepos = listOf(
    RepoCache(
        id = 2L,
        userPhotoUrl = "fake photo",
        userName = "fakeName",
        repoName = "fake name",
        programmingLanguage = "lang",
        stars = 23,
    ), RepoCache(
        id = 3L,
        userPhotoUrl = "fake photo",
        userName = "fakeName",
        repoName = "fake name",
        programmingLanguage = "lang",
        stars = 23,
    )
)


private class FakeDao : UserRepoDao {

    private var list = mutableListOf<RepoCache>()

    override suspend fun readUserRepos(): List<RepoCache> {
        return list
    }

    override suspend fun addUserRepos(userRepos: List<RepoCache>) {
        list.addAll(userRepos)
    }

    fun checkAddIsCalled(expectedList: List<RepoCache>) {
        assertEquals(expectedList, list)
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


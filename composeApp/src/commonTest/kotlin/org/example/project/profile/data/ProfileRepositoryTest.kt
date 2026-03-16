package org.example.project.profile.data

import kotlinx.coroutines.runBlocking
import org.example.project.core.cache.DataStoreManager
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileDao
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProfileRepositoryTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var profileDao: FakeProfileDao
    private lateinit var profileGithubApi: FakeProfileGithubApi
    private lateinit var dataStoreManager: FakeDataStoreManager
    private lateinit var fakeRepoDao: FakeRepoDao

    @BeforeTest
    fun setUp() {
        fakeRepoDao = FakeRepoDao()
        dataStoreManager = FakeDataStoreManager()
        profileDao = FakeProfileDao()
        profileGithubApi = FakeProfileGithubApi()
        profileRepository = ProfileRepositoryImpl(
            profileDao = profileDao,
            githubApi = profileGithubApi,
            dataStoreManager = dataStoreManager,
            userReposDao = fakeRepoDao,

        )
    }

    @Test
    fun `first load user profile then cache`() = runBlocking {
        profileGithubApi.isFailure(false)
        profileDao.mockCache(null)

        val actual = profileRepository.loadUserProfile()
        val expected = Result.success(expectedSuccessResult)

        assertEquals(expected, actual)
        profileDao.checkSaveCalled(profileCache)
    }

    @Test
    fun `load user profile from cache`() = runBlocking {
        profileGithubApi.isFailure(false)
        profileDao.mockCache(profileCache)

        val actualResult = profileRepository.loadUserProfile()
        val expected = Result.success(expectedSuccessResult)

        assertEquals(expected, actualResult)
    }

    @Test
    fun `success fetch user profile then cache`() = runBlocking {
        profileGithubApi.isFailure(false)
        profileDao.mockCache(null)

        val actualResult = profileRepository.refreshUserProfile()
        val expectedResult = Result.success(
            expectedSuccessResult
        )

        assertEquals(expectedResult, actualResult)
        profileDao.checkSaveCalled(expectedCache = profileCache)
    }

    @Test
    fun `failure fetch but cache is contain`() = runBlocking {
        profileGithubApi.isFailure(true)
        profileDao.mockCache(cache = profileCache)

        val actualResult = profileRepository.refreshUserProfile()
        val expectedResult = Result.success(expectedSuccessResult)

        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `failure fetch but cache doesnt contain`() = runBlocking {
        profileGithubApi.isFailure(true)
        profileDao.mockCache(null)

        val actualResult = profileRepository.refreshUserProfile()
        assertTrue(actualResult.isFailure)

        val exception = actualResult.exceptionOrNull()!!
        assertEquals(FAKE_MESSAGE, exception.message)
    }

    @Test
    fun logout() = runBlocking {
        profileRepository.logout()
        dataStoreManager.checkSaveIsCalled("")
        profileDao.checkClearAllCalled(1)
        fakeRepoDao.checkClearIsCalled(1)
    }

    companion object {
        private const val FAKE_MESSAGE = "fake message"
    }
}

private val expectedSuccessResult = Profile(
    avatar = "fakeAvatar",
    userName = "fakeName",
    bio = "fake bio",
    repoCount = 12,
    subscribersCount = 23,
)
private val successProfileData = ProfileData(
    userId = 1L,
    avatar = "fakeAvatar",
    userName = "fakeName",
    bio = "fake bio",
    repoCount = 12,
    subscribersCount = 23,
)

private val profileCache = ProfileCache(
    userId = 1L,
    avatar = "fakeAvatar",
    userName = "fakeName",
    bio = "fake bio",
    repoCount = 12,
    subscribersCount = 23,
)

private class FakeProfileGithubApi : ProfileGithubApi {

    private var isFailure = false

    override suspend fun userProfile(userToken: String): ProfileData {

        return if (isFailure) {
            throw IllegalStateException("fake message")
        } else {
            successProfileData
        }

    }

    fun isFailure(failureFlag: Boolean) {
        isFailure = failureFlag
    }
}

private class FakeDataStoreManager : DataStoreManager.TokenOperations {

    private var savedToken: String? = "mockSavedToken"

    override suspend fun saveUserToken(token: String) {
        savedToken = token
    }

    fun checkSaveIsCalled(expected: String) {
        assertEquals(expected, savedToken)
    }

    override suspend fun userToken(): String? {
        return savedToken
    }
}

private class FakeProfileDao : ProfileDao {

    private var profileCache: ProfileCache? = null

    private var clearCalledTimes = 0

    fun checkClearAllCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, clearCalledTimes)
    }

    fun mockCache(cache: ProfileCache?) {
        profileCache = cache
    }

    fun checkSaveCalled(expectedCache: ProfileCache?) {
        assertEquals(expectedCache, profileCache)
    }

    override suspend fun saveUserProfile(profileCache: ProfileCache) {
        this.profileCache = profileCache
    }

    override suspend fun readUserProfile(): ProfileCache? {
        return profileCache
    }

    override suspend fun clearAll() {
        clearCalledTimes++
    }

}

private class FakeRepoDao : UserRepoDao.ClearAll {
    private var clearCalledTimes = 0
    override suspend fun clearAll() {
        clearCalledTimes++
    }

    fun checkClearIsCalled(expectedTimes: Int) {
        assertEquals(
            expectedTimes, clearCalledTimes
        )
    }
}


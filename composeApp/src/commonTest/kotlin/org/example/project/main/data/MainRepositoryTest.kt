package org.example.project.main.data

import kotlinx.coroutines.runBlocking
import org.example.project.MockData
import org.example.project.core.cache.DataStoreManager
import org.example.project.core.cloud.FakeGithubApi
import org.example.project.main.domain.MainRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainRepositoryTest {
    private lateinit var mainRepository: MainRepository
    private lateinit var fakeGithubApi: FakeGithubApi
    private lateinit var dataStoreManager: FakeDataStoreManager

    @BeforeTest
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
        fakeGithubApi = FakeGithubApi()
        mainRepository =
            MainRepositoryImpl(githubApi = fakeGithubApi, dataStoreManager = dataStoreManager)
    }

    @Test
    fun `success query`() = runBlocking {

        val expectedResult = Result.success(MockData.mockedSearchResults)
        val actualResult = mainRepository.searchByQuery(FAKE_QUERY, 1)

        assertEquals(expectedResult, actualResult)
        dataStoreManager.checkCalledTimes(1)

    }

    @Test
    fun `success user repositories`() = runBlocking {
        val actualResult = mainRepository.userRepo(1)
        val expectedResult = Result.success(MockData.mockedRepositories)

        assertEquals(expectedResult, actualResult)
        dataStoreManager.checkCalledTimes(1)
    }

    @Test
    fun `failure query`() = runBlocking {
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.searchByQuery(FAKE_QUERY, 1)
        val actualException = actualResult.exceptionOrNull()!!
        assertEquals(FAKE_EXCEPTION_MESSAGE, actualException.message)
        dataStoreManager.checkCalledTimes(1)
    }

    @Test
    fun `failure user repositories`() = runBlocking {
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.userRepo(1)
        val actualException = actualResult.exceptionOrNull()!!
        assertEquals(FAKE_EXCEPTION_MESSAGE, actualException.message)
        dataStoreManager.checkCalledTimes(1)
    }

    companion object {
        private const val FAKE_QUERY = "fake query"
        private const val FAKE_EXCEPTION_MESSAGE = "fake message"
    }
}

private class FakeDataStoreManager : DataStoreManager.ReadToken {

    private var actualCalledTimes = 0
    override suspend fun userToken(): String? {
        actualCalledTimes++
        return "fakeToken"
    }


    fun checkCalledTimes(expectedCalledTimes: Int) {
        assertEquals(expectedCalledTimes, actualCalledTimes)
    }
}

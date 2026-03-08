package org.example.project.main

import kotlinx.coroutines.runBlocking
import org.example.project.MockData
import org.example.project.main.data.MainRepositoryImpl
import org.example.project.main.data.cloud.FakeGithubApi
import org.example.project.main.domain.MainRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainRepositoryTest {
    private lateinit var mainRepository: MainRepository
    private lateinit var fakeGithubApi: FakeGithubApi

    @BeforeTest
    fun setUp() {
        fakeGithubApi = FakeGithubApi()
        mainRepository = MainRepositoryImpl(githubApi = fakeGithubApi)
    }

    @Test
    fun `success query`() = runBlocking {

        val expectedResult = Result.success(MockData.mockedSearchResults)
        val actualResult = mainRepository.searchByQuery(FAKE_QUERY)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `success user repositories`() = runBlocking {
        val actualResult = mainRepository.userRepo()
        val expectedResult = Result.success(MockData.mockedRepositories)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `failure query`() = runBlocking {
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.searchByQuery(FAKE_QUERY)
        val actualException = actualResult.exceptionOrNull()!!
        assertEquals(FAKE_EXCEPTION_MESSAGE, actualException.message)
    }

    @Test
    fun `failure user repositories`() = runBlocking {
        fakeGithubApi.setException(IllegalStateException(FAKE_EXCEPTION_MESSAGE))

        val actualResult = mainRepository.userRepo()
        val actualException = actualResult.exceptionOrNull()!!
        assertEquals(FAKE_EXCEPTION_MESSAGE, actualException.message)
    }

    companion object {
        private const val FAKE_QUERY = "fake query"
        private const val FAKE_EXCEPTION_MESSAGE = "fake message"
    }
}

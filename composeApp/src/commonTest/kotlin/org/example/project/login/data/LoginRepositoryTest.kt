package org.example.project.login.data

import kotlinx.coroutines.runBlocking
import org.example.project.FakeAuthWrapper
import org.example.project.core.cache.DataStoreManager
import org.example.project.login.domain.LoginRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginRepositoryTest {
    private lateinit var loginRepository: LoginRepository
    private lateinit var authWrapper: FakeAuthWrapper
    private lateinit var fakeDataStoreManager: FakeDataStoreManager

    @BeforeTest
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        authWrapper = FakeAuthWrapper()
        loginRepository =
            LoginRepositoryImpl(authWrapper = authWrapper, dataStoreManager = fakeDataStoreManager)
    }

    @Test
    fun success() = runBlocking {
        val actualResult = loginRepository.userToken()
        val expectedResult = Result.success(FAKE_TOKEN)

        assertEquals(expectedResult, actualResult)

        loginRepository.saveUserToken(FAKE_TOKEN)

        fakeDataStoreManager.checkSaveCalled(expectedSavedToken = "fakeToken", 1)
    }

    @Test
    fun failure() = runBlocking {
        val expectedException = IllegalStateException(FAKE_ERROR_MESSAGE)
        authWrapper.setException(expectedException)

        val actualResult = loginRepository.userToken()
        assertTrue(actualResult.isFailure)
        val error = actualResult.exceptionOrNull()!!
        assertEquals(expectedException.message, error.message)
    }

    companion object {
        private const val FAKE_TOKEN = "fakeToken"
        private const val FAKE_ERROR_MESSAGE = "fake error message"
    }
}

private class FakeDataStoreManager : DataStoreManager.SaveToken {

    private lateinit var savedToken: String
    private var actualCalledTimes = 0

    override suspend fun saveUserToken(token: String) {
        actualCalledTimes++
        savedToken = token

    }

    fun checkSaveCalled(expectedSavedToken: String, expectedCalledTimes: Int) {
        assertEquals(expectedSavedToken, savedToken)
        assertEquals(expectedCalledTimes, actualCalledTimes)
    }

}

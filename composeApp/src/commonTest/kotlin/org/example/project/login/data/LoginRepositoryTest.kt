package org.example.project.login.data

import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.runBlocking
import org.example.project.FakeAuthWrapper
import org.example.project.core.data.HandleDomainError
import org.example.project.core.data.RunCatchingSuspend
import org.example.project.core.data.cache.DataStoreManager
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.FakeManageResource
import org.example.project.login.domain.LoginRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginRepositoryTest {
    private lateinit var loginRepository: LoginRepository
    private lateinit var authWrapper: FakeAuthWrapper
    private lateinit var fakeDataStoreManager: FakeDataStoreManager
    private lateinit var fakeManageResource: FakeManageResource

    @BeforeTest
    fun setUp() {
        fakeManageResource = FakeManageResource()
        fakeDataStoreManager = FakeDataStoreManager()
        authWrapper = FakeAuthWrapper()
        loginRepository =
            LoginRepositoryImpl(
                authWrapper = authWrapper, dataStoreManager = fakeDataStoreManager,
                customRunCatching = RunCatchingSuspend(handleDomainError = HandleDomainError.Base())
            )
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
    fun `failure types`() = runBlocking {
        val expectedException = IllegalStateException(FAKE_ERROR_MESSAGE)
        authWrapper.setException(true, expectedException)

        var actualResult = loginRepository.userToken()
            .onFailure {
                val error = it as DomainException
                val message = error.exceptionString(fakeManageResource)
                assertEquals(message, "service unavailable")
            }
        assertTrue(actualResult.isFailure)

        authWrapper.setException(true, SocketTimeoutException(""))


        actualResult = loginRepository.userToken()
            .onFailure {
                val error = it as DomainException
                val message = error.exceptionString(fakeManageResource)
                assertEquals("no connection", message)
            }
        assertTrue(actualResult.isFailure)


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

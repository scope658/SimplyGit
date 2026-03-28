package org.example.project.login

import kotlinx.coroutines.runBlocking
import org.example.project.FakeAuthWrapper
import org.example.project.TokenStorage
import org.example.project.login.data.LoginRepositoryImpl
import org.example.project.login.domain.LoginRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginRepositoryTest {
    private lateinit var loginRepository: LoginRepository
    private lateinit var authWrapper: FakeAuthWrapper

    @BeforeTest
    fun setUp() {
        authWrapper = FakeAuthWrapper()
        loginRepository = LoginRepositoryImpl(authWrapper = authWrapper)
    }

    @Test
    fun success() = runBlocking {
        val actualResult = loginRepository.userToken()
        val expectedResult = Result.success(FAKE_TOKEN)
        assertEquals(expectedResult, actualResult)

        val currentToken = TokenStorage.token
        assertEquals("", currentToken)

        loginRepository.saveUserToken(FAKE_TOKEN)
        val actualSavedToken = TokenStorage.token
        assertEquals(FAKE_TOKEN, actualSavedToken)
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


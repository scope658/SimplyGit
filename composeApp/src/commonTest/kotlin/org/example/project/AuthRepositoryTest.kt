package org.example.project

import kotlinx.coroutines.runBlocking
import org.example.project.login.data.LoginRepositoryImpl
import org.example.project.login.domain.LoginRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryTest {
    private lateinit var authRepository: LoginRepository
    private lateinit var authWrapper: FakeAuthWrapper

    @BeforeTest
    fun setUp() {
        authWrapper = FakeAuthWrapper()
        authRepository = LoginRepositoryImpl(authWrapper = authWrapper)
    }

    @Test
    fun success() = runBlocking {
        val actualResult = authRepository.userToken()
        val expectedResult = Result.success("fakeToken")
        assertEquals(expectedResult, actualResult)

        val currentToken = TokenStorage.token
        assertEquals("", currentToken)

        authRepository.saveUserToken("fakeToken")
        val actualSavedToken = TokenStorage.token
        assertEquals("fakeToken", actualSavedToken)
    }

    @Test
    fun failure() = runBlocking {
        val expectedException = IllegalStateException("fake error message")
        authWrapper.setException(expectedException)

        val actualResult = authRepository.userToken()
        assertTrue(actualResult.isFailure)
        val error = actualResult.exceptionOrNull()!!
        assertEquals(expectedException.message, error.message)
    }

}


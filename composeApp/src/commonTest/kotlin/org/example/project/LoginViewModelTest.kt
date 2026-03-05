package org.example.project

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.example.project.core.FakeRunAsync
import org.example.project.core.RunAsync
import org.example.project.login.domain.LoginRepository
import org.example.project.login.presentation.ErrorState
import org.example.project.login.presentation.LoginUiEvent
import org.example.project.login.presentation.LoginUiState
import org.example.project.login.presentation.LoginViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginViewModelTest {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: FakeLoginRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var fakeLoginRunAsync: FakeLoginRunAsync

    @BeforeTest
    fun setUp() {
        fakeLoginRunAsync = FakeLoginRunAsync()
        savedStateHandle = SavedStateHandle()
        loginRepository = FakeLoginRepository()
        loginViewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            runAsync = fakeLoginRunAsync
        )
    }

    @Test
    fun `failure login then success`() = runBlocking {
        loginRepository.isFailure(true)
        val loginUiState: StateFlow<LoginUiState> = loginViewModel.loginUiState

        assertEquals(initialUiState, loginUiState.value)

        loginViewModel.loginIn()

        assertEquals(loadingUiState, loginUiState.value)
        fakeLoginRunAsync.invokeBackground()

        assertEquals(errorUiState, loginUiState.value)

        //process death
        loginViewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            runAsync = fakeLoginRunAsync
        )

        val newState = loginViewModel.loginUiState.value
        assertEquals(errorUiState, newState)

        val sharedFlow: SharedFlow<LoginUiEvent> = loginViewModel.loginUiEvent
        val actualEvent = withTimeoutOrNull(300) {
            sharedFlow.first()
        }
        assertNull(actualEvent)

        loginRepository.isFailure(false)

        loginViewModel.loginIn()

        assertEquals(loadingUiState, loginUiState.value)
        fakeLoginRunAsync.invokeBackground()
        loginRepository.checkSavedToken("fakeToken")
        assertEquals(loadingUiState, loginUiState.value)

        val newSharedFlow: SharedFlow<LoginUiEvent> = loginViewModel.loginUiEvent
        val actualNewEvent = withTimeout(300) {
            newSharedFlow.first()
        }
        assertEquals(LoginUiEvent.LoginSuccessEvent, actualNewEvent)
    }

}

private val loadingUiState = LoginUiState.Loading
private val initialUiState = LoginUiState.Initial(errorState = ErrorState.Empty)
private val errorUiState =
    LoginUiState.Initial(errorState = ErrorState.Error(message = "auth failed"))


private class FakeLoginRunAsync(
    private val base: FakeRunAsync = FakeRunAsync()
) : RunAsync by base {

    private lateinit var cachedBackground: suspend () -> Any

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        cachedBackground = background
    }

    fun invokeBackground() = runBlocking {
        cachedBackground.invoke()
    }

}

private class FakeLoginRepository : LoginRepository {

    private var mockedResult = Result.success("fakeToken")
    private var failureFlag = false
    private lateinit var savedToken: String
    override suspend fun userToken(): Result<String> {

        return if (failureFlag) {
            Result.failure(IllegalStateException("auth failed"))
        } else {
            mockedResult
        }

    }

    override suspend fun saveUserToken(token: String) {
        savedToken = token
    }

    fun isFailure(flag: Boolean) {
        failureFlag = flag
    }

    fun checkSavedToken(expectedSavedToken: String) {
        assertEquals(savedToken, expectedSavedToken)
    }
}

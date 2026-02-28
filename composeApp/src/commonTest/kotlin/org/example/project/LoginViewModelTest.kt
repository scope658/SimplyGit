package org.example.project

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.example.project.core.FakeRunAsync
import org.example.project.login.ErrorState
import org.example.project.login.LoginUiEvent
import org.example.project.login.LoginUiState
import org.example.project.login.LoginViewModel
import org.example.project.login.domain.LoginRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginViewModelTest {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: FakeLoginRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var fakeLoginRunAsync: FakeRunAsync

    @BeforeTest
    fun setUp() {
        fakeLoginRunAsync = FakeRunAsync()
        savedStateHandle = SavedStateHandle()
        loginRepository = FakeLoginRepository()
        loginViewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            runAsync = fakeLoginRunAsync
        )
    }

    @Test
    fun `login is valid`() = runBlocking {
        loginRepository.mockResult(Result.success(Unit))
        val loginUiState: StateFlow<LoginUiState> = loginViewModel.loginUiState

        assertEquals(initialLoginUiState, loginUiState.value)


        loginViewModel.onUsernameChanged(userName = "admin")
        loginViewModel.onPasswordChanged(password = "admin1234")

        assertEquals(successLoginUiState, loginUiState.value)
        loginViewModel.login()

        val loginEventSharedFlow: SharedFlow<LoginUiEvent> = loginViewModel.loginUiEvent

        val actualEvent = withTimeout(1000) {
            loginEventSharedFlow.first()
        }

        val expectedEvent = LoginUiEvent.LoginSuccessEvent
        assertEquals(expectedEvent, actualEvent)
    }

    @Test
    fun `login is empty`() {
        val loginUiState: StateFlow<LoginUiState> = loginViewModel.loginUiState

        loginViewModel.onUsernameChanged(userName = " ")
        loginViewModel.onPasswordChanged(password = " ")

        assertEquals(initialLoginUiState, loginUiState.value)
    }

    @Test
    fun `login is not valid then valid`() = runBlocking {
        loginRepository.mockResult(Result.failure(IllegalStateException()))

        val loginEventSharedFlow: SharedFlow<LoginUiEvent> = loginViewModel.loginUiEvent
        val loginUiState: StateFlow<LoginUiState> = loginViewModel.loginUiState

        loginViewModel.onUsernameChanged(userName = "a")

        assertEquals(
            failureLoginUiState.copy(
                password = "",
                error = ErrorState.Empty,
                isLoginButtonActive = false
            ),
            loginUiState.value
        )

        loginViewModel.onPasswordChanged("1")
        assertEquals(failureLoginUiState.copy(error = ErrorState.Empty), loginUiState.value)

        val actualEvent = withTimeoutOrNull(300) {
            loginEventSharedFlow.first()
        }
        assertNull(actualEvent)

        loginViewModel.login()

        assertEquals(failureLoginUiState, loginUiState.value)

        loginRepository.mockResult(Result.success(Unit))

        loginViewModel.onUsernameChanged(userName = "admin")
        loginViewModel.onPasswordChanged(password = "admin1234")

        assertEquals(successLoginUiState.copy(error = ErrorState.Error), loginUiState.value)

        //process death
        loginViewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            runAsync = fakeLoginRunAsync
        )

        val newLoginUiState: StateFlow<LoginUiState> = loginViewModel.loginUiState

        assertEquals(successLoginUiState.copy(error = ErrorState.Error), newLoginUiState.value)

        loginViewModel.login()

        val newLoginEventSharedFlow: SharedFlow<LoginUiEvent> = loginViewModel.loginUiEvent
        val actualFinalEvent = withTimeout(300) {
            newLoginEventSharedFlow.first()
        }

        val expectedEvent = LoginUiEvent.LoginSuccessEvent
        assertEquals(expectedEvent, actualFinalEvent)
    }
}

private val failureLoginUiState = LoginUiState(
    login = "a",
    password = "1",
    isLoginButtonActive = true,
    error = ErrorState.Error,
)


private val successLoginUiState = LoginUiState(
    login = "admin",
    password = "admin1234",
    isLoginButtonActive = true,
    error = ErrorState.Empty,

    )
private val initialLoginUiState = LoginUiState(
    login = "",
    password = "",
    isLoginButtonActive = false,
    error = ErrorState.Empty,
)

private class FakeLoginRepository : LoginRepository {

    private var fakeResult: Result<Unit> = Result.success(Unit)

    override fun login(username: String, password: String): Result<Unit> {
        return fakeResult
    }

    fun mockResult(result: Result<Unit>) {
        fakeResult = result
    }

}

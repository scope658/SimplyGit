package org.example.project.login.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.core.RunAsync
import org.example.project.login.domain.LoginRepository

class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val loginRepository: LoginRepository,
    private val runAsync: RunAsync,
) : ViewModel(), LoginActions {

    private val _loginUiState: MutableStateFlow<LoginUiState> =
        savedStateHandle.getMutableStateFlow(
            LOGIN_UI_STATE_KEY,
            initialValue = LoginUiState.Initial(errorState = ErrorState.Empty)
        )
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginUiEvent: MutableSharedFlow<LoginUiEvent> = MutableSharedFlow(replay = 1)
    val loginUiEvent = _loginUiEvent.asSharedFlow()


    override fun loginIn() {

        _loginUiState.value = LoginUiState.Loading
        runAsync.runAsync(
            scope = viewModelScope,
            {
                loginRepository.userToken()
                    .onSuccess { userToken ->
                        loginRepository.saveUserToken(token = userToken)
                        _loginUiEvent.emit(LoginUiEvent.LoginSuccessEvent)
                    }
                    .onFailure {
                        _loginUiState.value =
                            LoginUiState.Initial(
                                errorState = ErrorState.Error(
                                    message = it.message ?: "something went wrong"
                                )
                            )
                    }
            },
            {}
        )
    }

    companion object {
        private const val LOGIN_UI_STATE_KEY = "LOGIN_UI_STATE_KEY"
    }

}

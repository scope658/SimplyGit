package org.example.project.login.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize
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
            initialValue = LoginUiState.INITIAL
        )
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginUiEvent: MutableSharedFlow<LoginUiEvent> = MutableSharedFlow(replay = 1)
    val loginUiEvent = _loginUiEvent.asSharedFlow()

    override fun onUsernameChanged(userName: String) {
        val trimmedUserName = userName.trim()
        _loginUiState.update {
            it.copy(
                login = trimmedUserName,
                isLoginButtonActive = validate(trimmedUserName, password = it.password)
            )
        }
    }

    override fun onPasswordChanged(password: String) {
        val trimmedPassword = password.trim()
        _loginUiState.update {
            it.copy(
                password = trimmedPassword,
                isLoginButtonActive = validate(it.login, password = trimmedPassword)
            )
        }
    }

    override fun login() {
        val loginUiState = _loginUiState.value
        val login = loginUiState.login
        val password = loginUiState.password
        loginRepository.login(login = login, password = password)
            .onSuccess {
                runAsync.runSharedFlow(
                    scope = viewModelScope,
                    action = { _loginUiEvent.emit(LoginUiEvent.LoginSuccessEvent) }
                )
            }
            .onFailure {
                _loginUiState.update {
                    it.copy(error = ErrorState.Error)
                }
            }
    }

    companion object {
        private const val LOGIN_UI_STATE_KEY = "LOGIN_UI_STATE_KEY"
    }

    private fun validate(userName: String, password: String): Boolean {
        return userName.isNotBlank() && password.isNotBlank()
    }
}

@CommonParcelize
data class LoginUiState(
    val login: String,
    val password: String,
    val isLoginButtonActive: Boolean,
    val error: ErrorState,
) : CommonParcelable {
    companion object {
        val INITIAL = LoginUiState(login = "", "", false, ErrorState.Empty)
    }
}

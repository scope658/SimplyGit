package org.example.project.login.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.core.presentation.RunAsync
import org.example.project.login.domain.LoginRepository

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val loginRepository: LoginRepository,
    private val runAsync: RunAsync,
    private val manageResource: ManageResource
) : ViewModel(), LoginActions {
    private var savedState: LoginUiState by savedStateHandle.saved(
        key = LOGIN_UI_STATE_KEY,
        init = { LoginUiState.Initial(errorState = ErrorState.Empty) }
    )
    private val _loginUiState = MutableStateFlow(value = savedState)
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginUiEvent: MutableSharedFlow<LoginUiEvent> = MutableSharedFlow()
    val loginUiEvent = _loginUiEvent.asSharedFlow()

    override fun loginIn() {
        savedState = LoginUiState.Loading
        _loginUiState.value = savedState
        runAsync.runAsync(
            scope = viewModelScope,
            {
                loginRepository.userToken()
            },
            { result ->
                result.onSuccess { userToken ->
                    loginRepository.saveUserToken(token = userToken)
                    _loginUiEvent.emit(LoginUiEvent.LoginSuccessEvent)

                }.onFailure {
                    val exception = it as? DomainException ?: ServiceUnavailableException
                    val message = exception.exceptionString(manageResource)
                    savedState =
                        LoginUiState.Initial(
                            errorState = ErrorState.Error(
                                message = message
                            )
                        )
                    _loginUiState.value = savedState
                }
            }
        )
    }

    companion object {
        private const val LOGIN_UI_STATE_KEY = "LOGIN_UI_STATE_KEY"
    }
}

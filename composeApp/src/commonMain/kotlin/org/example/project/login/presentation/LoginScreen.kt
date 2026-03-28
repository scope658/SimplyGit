package org.example.project.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.example.project.login.presentation.screens.InitialLoginScreen
import org.example.project.login.presentation.screens.LoadingLoginScreen
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
) {
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.loginUiEvent.collectLatest { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> onLoginSuccess()
            }
        }
    }

    when (val state = loginUiState) {
        is LoginUiState.Initial -> InitialLoginScreen(state.errorState, actions = loginViewModel)
        is LoginUiState.Loading -> LoadingLoginScreen()
    }
}


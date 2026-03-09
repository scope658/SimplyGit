package org.example.project.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
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
    loginUiState.Show(loginViewModel)
}

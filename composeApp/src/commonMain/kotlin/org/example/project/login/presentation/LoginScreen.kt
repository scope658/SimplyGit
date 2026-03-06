package org.example.project.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import org.example.project.Routes
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    navController: NavController, loginViewModel: LoginViewModel = koinViewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.loginUiEvent.collectLatest { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> navController.navigate(Routes.Main) {
                    popUpTo(Routes.Login) {
                        inclusive = true
                    }
                }
            }
        }
    }
    loginUiState.Show(loginViewModel)
}

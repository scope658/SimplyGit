package org.example.project.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    LoginUi(
        loginUiState,
        loginViewModel,
    )

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
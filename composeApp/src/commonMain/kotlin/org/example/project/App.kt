package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.login.presentation.LoginScreen
import org.example.project.main.presentation.MainScreen
import org.example.project.onboarding.presentation.OnboardingScreen
import theme.CatAppTheme

sealed interface Routes {

    @Serializable
    object Onboarding : Routes

    @Serializable
    object Login : Routes

    @Serializable
    object Main : Routes
}

@Composable
fun App() {
    val navController = rememberNavController()
    CatAppTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Onboarding,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Routes.Onboarding> { OnboardingScreen(navController) }
                composable<Routes.Login> { LoginScreen(navController) }
                composable<Routes.Main> { MainScreen() }
            }
        }
    }
}
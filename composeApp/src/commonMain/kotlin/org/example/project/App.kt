package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import theme.CatAppTheme

sealed interface Routes {

    @Serializable
    object Onboarding : Routes

    @Serializable
    object Login : Routes

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
                composable<Routes.Onboarding> {
                    OnboardingScreen() {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.Onboarding) {
                                inclusive = true
                            }
                        }
                    }
                }
                composable<Routes.Login> { LoginScreen() }
            }
        }
    }
}
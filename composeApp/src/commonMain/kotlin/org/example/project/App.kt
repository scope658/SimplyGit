package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.app.presentation.AppScreen
import org.example.project.login.presentation.LoginScreen
import org.example.project.main.presentation.MainScreen
import org.example.project.onboarding.presentation.OnboardingScreen
import theme.CatAppTheme

sealed interface Routes {

    @Serializable
    object App : Routes

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
                startDestination = Routes.App,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Routes.App> {
                    AppScreen(
                        onLoginIn = { firstRunNavigate(route = Routes.Login, navController) },
                        onOnboarding = {
                            firstRunNavigate(
                                route = Routes.Onboarding,
                                navController
                            )
                        },
                        onMain = { firstRunNavigate(route = Routes.Main, navController) },
                    )
                }
                composable<Routes.Onboarding> {
                    OnboardingScreen(onOnboardingFinished = {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.Onboarding) {
                                inclusive = true
                            }
                        }
                    })
                }

                composable<Routes.Login> {
                    LoginScreen(onLoginSuccess = {
                        navController.navigate(Routes.Main) {
                            popUpTo(Routes.Login) {
                                inclusive = true
                            }
                        }
                    })
                }

                composable<Routes.Main> { MainScreen() }
            }
        }
    }
}

private fun firstRunNavigate(route: Routes, navController: NavController) {
    navController.navigate(route) {
        popUpTo<Routes.App>() {
            inclusive = true
        }
    }
}

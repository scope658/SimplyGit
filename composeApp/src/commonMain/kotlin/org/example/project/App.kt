package org.example.project

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.app.presentation.AppScreen
import org.example.project.bottomNav.MainBottomNavContainer
import org.example.project.createIssues.presentation.CreateIssuesScreen
import org.example.project.details.presentation.DetailsScreen
import org.example.project.login.presentation.LoginScreen
import org.example.project.onboarding.presentation.OnboardingScreen
import org.example.project.repoFiles.presentation.RepoFilesScreen
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

    @Serializable
    object Profile : Routes

    @Serializable
    data class Details(val repoOwner: String, val repoName: String) : Routes


    @Serializable
    data class CreateIssue(val repoOwner: String, val repoName: String) : Routes


    @Serializable
    data class RepoFiles(val repoOwner: String, val repoName: String, val path: String = "") :
        Routes

}

@Composable
fun App() {
    val navController = rememberNavController()
    CatAppTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.App,
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

                composable<Routes.Main> {
                    MainBottomNavContainer(navController)
                }
                composable<Routes.Details> {
                    DetailsScreen(
                        onCreateIssues = { repoOwner, repoName ->
                            navController.navigate(
                                Routes.CreateIssue(
                                    repoOwner = repoOwner,
                                    repoName = repoName
                                )
                            )
                        },
                        onCode = { repoOwner, repoName ->
                            navController.navigate(Routes.RepoFiles(repoOwner, repoName))
                        }
                    )
                }
                composable<Routes.CreateIssue> { CreateIssuesScreen(onSuccess = { navController.popBackStack() }) }
                composable<Routes.RepoFiles> {
                    RepoFilesScreen(
                        onNextFiles = { repoOwner, repoName, path ->
                            navController.navigate(Routes.RepoFiles(repoOwner, repoName, path))
                        }
                    )
                }
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

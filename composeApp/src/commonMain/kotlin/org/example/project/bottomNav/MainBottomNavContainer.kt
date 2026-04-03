package org.example.project.bottomNav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.Routes
import org.example.project.main.presentation.MainScreen
import org.example.project.profile.presentation.ProfileScreen

@Composable
fun MainBottomNavContainer(outerNavController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
    ) { padding ->
        NavHost(
            navController,
            startDestination = "search",
            modifier = Modifier.padding(padding)
        ) {
            composable("search") { MainScreen(onProfileClick = {}) }
            composable("favourites") { }
            composable("profile") {
                ProfileScreen(onLogout = {
                    outerNavController.navigate(Routes.Login) {
                        popUpTo(Routes.Main) {
                            inclusive = true
                        }
                    }
                })
            }
        }
    }
}

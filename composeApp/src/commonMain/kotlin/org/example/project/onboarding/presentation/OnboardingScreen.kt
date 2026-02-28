package org.example.project.onboarding.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import org.example.project.Routes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val onboardingPage by viewModel.onboardingPageFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onboardingEvent.collectLatest { event ->
            when (event) {
                is OnboardingEvent.Finished -> navController.navigate(Routes.Login) {
                    popUpTo(Routes.Onboarding) {
                        inclusive = true
                    }
                }
            }
        }
    }
    OnboardingUi(
        onboardingPage = onboardingPage,
        onboardingActions = viewModel,
    )
}

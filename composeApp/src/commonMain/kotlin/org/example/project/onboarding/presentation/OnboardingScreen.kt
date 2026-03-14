package org.example.project.onboarding.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel(),
    onOnboardingFinished: () -> Unit,
) {
    val onboardingPage by viewModel.onboardingPageFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onboardingEvent.collectLatest { event ->
            when (event) {
                is OnboardingEvent.Finished -> {
                    viewModel.finishOnboarding()
                    onOnboardingFinished()
                }
            }
        }
    }
    OnboardingUi(
        onboardingPage = onboardingPage,
        onboardingActions = viewModel,
    )
}

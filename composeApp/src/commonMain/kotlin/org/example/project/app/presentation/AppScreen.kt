package org.example.project.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppScreen(
    appViewModel: AppViewModel = koinViewModel(),
    onLoginIn: () -> Unit,
    onOnboarding: () -> Unit,
    onMain: () -> Unit
) {
    val eventSharedFlow = appViewModel.appEvent
    LaunchedEffect(Unit) {
        eventSharedFlow.collectLatest { appEvent ->
            when (appEvent) {
                AppEvent.OnLoginIn -> onLoginIn()
                AppEvent.OnMain -> onMain()
                AppEvent.OnOnboarding -> onOnboarding()
            }
        }
    }
}

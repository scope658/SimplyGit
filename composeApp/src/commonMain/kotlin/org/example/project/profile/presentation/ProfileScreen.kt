package org.example.project.profile.presentation

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.example.project.core.presentation.GeneralFailureScreen
import org.example.project.core.presentation.GeneralLoadingIndicator
import org.example.project.profile.presentation.screens.ProfileSuccessScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(), onLogout: () -> Unit
) {
    val uiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val event = profileViewModel.profileEvent

    LaunchedEffect(Unit) {
        event
            .collectLatest {
                when (it) {
                    is ProfileEvent.Logout -> onLogout()
                }
            }
    }
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { profileViewModel.refresh() }
    ) {
        when (val state = uiState.profileUiState) {
            is ProfileUiState.Failure -> GeneralFailureScreen(
                state.message,
                retryAction = { profileViewModel.retry() })

            is ProfileUiState.Loading -> GeneralLoadingIndicator()
            is ProfileUiState.Success -> ProfileSuccessScreen(
                state,
                profileViewModel
            )
        }
    }
}

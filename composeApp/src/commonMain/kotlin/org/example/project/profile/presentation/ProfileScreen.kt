package org.example.project.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
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
}

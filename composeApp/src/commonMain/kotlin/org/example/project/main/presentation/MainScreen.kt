package org.example.project.main.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel(),
    onDetails: (repoOwner: String, repoName: String) -> Unit
) {
    val mainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()
    val searchText by mainViewModel.searchText.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mainViewModel.mainUiEvent.collectLatest { mainEvent ->
            when (mainEvent) {
                is MainEvent.OnDetails -> onDetails(mainEvent.repoOwner, mainEvent.repoName)
            }
        }
    }
    MainUi(
        mainUiState = mainUiState.mainUiState,
        searchText = searchText,
        mainActions = mainViewModel,
        isRefreshing = mainUiState.isRefreshing,
    )
}


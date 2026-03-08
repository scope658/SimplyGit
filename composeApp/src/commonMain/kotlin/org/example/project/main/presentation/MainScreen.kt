package org.example.project.main.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel = koinViewModel()) {
    val mainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()
    val searchText by mainViewModel.searchText.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mainViewModel.loadUserRepo()
    }
    MainUi(
        mainUiState = mainUiState,
        searchText = searchText,
        mainActions = mainViewModel,
    )
}

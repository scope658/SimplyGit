package org.example.project.main.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel = koinViewModel()) {
    val repositoriesUi by mainViewModel.userRepositoriesUi.collectAsStateWithLifecycle()
    MainUi(repositoriesUi)
}
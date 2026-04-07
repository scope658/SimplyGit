package org.example.project.details.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DetailsScreen(detailsViewModel: DetailsViewModel = koinViewModel()) {
    val detailsScreenState by detailsViewModel.detailsScreenState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        detailsViewModel.detailsEvent.collectLatest {
            when (it) {
                is DetailsEvent.OnCode -> Unit
                is DetailsEvent.OnCreateIssues -> Unit
            }
        }
    }
    DetailsUi(
        detailsScreenState.isRefreshing,
        detailsScreenState.detailsUiState,
        detailsViewModel
    )
}

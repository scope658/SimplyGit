package org.example.project.details.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.core.presentation.GeneralFailureScreen
import org.example.project.core.presentation.GeneralLoadingIndicator
import org.example.project.details.presentation.Screens.DetailsSuccessScreen

@Composable
fun DetailsUi(
    isRefreshing: Boolean,
    mainUiState: DetailsUiState,
    detailsActions: DetailsActions
) {
    Scaffold {
        PullToRefreshBox(
            isRefreshing,
            onRefresh = { detailsActions.refresh() },
            modifier = Modifier.padding(it)
        ) {
            when (val state = mainUiState) {
                is DetailsUiState.Failure -> GeneralFailureScreen(
                    state.message,
                    { detailsActions.retry() })

                is DetailsUiState.Loading -> GeneralLoadingIndicator()
                is DetailsUiState.Success -> DetailsSuccessScreen(mainUiState)
            }
        }
    }
}

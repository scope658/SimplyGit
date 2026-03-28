package org.example.project.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.retry
import org.example.project.main.presentation.MainActions
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.PagingUiState
import org.jetbrains.compose.resources.stringResource
import theme.pagingButtonRetryShape
import theme.spacingL
import theme.spacingS
import theme.spacingXXXL

fun LazyListScope.pagingFooter(
    mainActions: MainActions,
    mainUiState: MainUiState.Success
) {
    item(key = "paging_footer") {
        PagingFooter(mainUiState.pagingUiState, mainUiState, mainActions = mainActions)
    }
}

@Composable
private fun PagingFooter(
    pagingUiState: PagingUiState,
    mainUiState: MainUiState.Success,
    mainActions: MainActions
) {
    when (val state = pagingUiState) {
        is PagingUiState.Loading -> PagingLoading {
            mainActions.loadMore(
                isLoadMore = mainUiState.isLoadMore,
                currentRepoList = mainUiState.result,
                page = mainUiState.page
            )
        }

        PagingUiState.Empty -> Unit

        is PagingUiState.Failure -> PagingErrorContent(
            failureMessage = state.message,
            onScrolledToEnd = {
                mainActions.loadMore(
                    isLoadMore = mainUiState.isLoadMore,
                    currentRepoList = mainUiState.result,
                    page = mainUiState.page
                )
            })


    }
}

@Composable
private fun PagingLoading(onScrolledToEnd: () -> Unit) {
    LaunchedEffect(Unit) {
        onScrolledToEnd()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacingL),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(spacingXXXL))
    }
}

@Composable
private fun PagingErrorContent(
    failureMessage: String,
    onScrolledToEnd: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingS)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = failureMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = onScrolledToEnd,
            shape = pagingButtonRetryShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(stringResource(Res.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PagingLoadingPreview() {
    PagingLoading({})
}

@Preview(showBackground = true)
@Composable
private fun PagingFailurePreview() {
    PagingErrorContent(
        failureMessage = "message",
        onScrolledToEnd = {}
    )
}



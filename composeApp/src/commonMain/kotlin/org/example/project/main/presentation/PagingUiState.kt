package org.example.project.main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize
import org.jetbrains.compose.resources.stringResource
import theme.pagingButtonRetryShape
import theme.spacingL
import theme.spacingS
import theme.spacingXXXL

interface PagingUiState : CommonParcelable {


    fun show(

        lazyListScope: LazyListScope,
        onScrolledToEnd: () -> Unit
    )

    @CommonParcelize
    data class Failure(val message: String) : PagingUiState {
        override fun show(
            lazyListScope: LazyListScope,
            onScrolledToEnd: () -> Unit
        ) {
            lazyListScope.item {
                LaunchedEffect(Unit) {
                    onScrolledToEnd()

                }
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
                        text = message,
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
        }
    }

    @CommonParcelize
    object Loading : PagingUiState {
        override fun show(
            lazyListScope: LazyListScope,
            onScrolledToEnd: () -> Unit
        ) {
            lazyListScope.item {
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
        }

    }

    @CommonParcelize
    object Empty : PagingUiState {
        override fun show(
            lazyListScope: LazyListScope,
            onScrolledToEnd: () -> Unit
        ) = Unit

    }
}

@Preview(showBackground = true)
@Composable
private fun PagingFailureState() {
    LazyColumn {
        PagingUiState.Failure("something went wrong").show(this, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PagingLoadingState() {
    LazyColumn {
        PagingUiState.Loading.show(this, {})
    }
}

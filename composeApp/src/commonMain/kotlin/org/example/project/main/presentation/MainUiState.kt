package org.example.project.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.empty_icon_test_tag
import ktshwnumbertwo.composeapp.generated.resources.empty_result_text_test_tag
import ktshwnumbertwo.composeapp.generated.resources.main_error_message_test_tag
import ktshwnumbertwo.composeapp.generated.resources.main_lazy_column_test_tag
import ktshwnumbertwo.composeapp.generated.resources.main_retry_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.nothing_image
import ktshwnumbertwo.composeapp.generated.resources.nothing_to_see_here
import ktshwnumbertwo.composeapp.generated.resources.retry
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize
import org.example.project.MockData
import org.example.project.main.presentation.components.RepositoryCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.fontSizeS
import theme.fontSizeXXL
import theme.progressIndicatorLarge


interface MainUiState : CommonParcelable {
    @Composable
    fun Show(mainActions: MainActions)

    @CommonParcelize
    data class Success(
        val page: Int,
        val isLoadMore: Boolean,
        val pagingUiState: PagingUiState,
        val result: List<UserRepositoryUi>
    ) : MainUiState {
        @Composable
        override fun Show(mainActions: MainActions) {

            LazyColumn(modifier = Modifier.testTag(stringResource(Res.string.main_lazy_column_test_tag))) {
                items(items = result, key = { it.id }) { userRepositoryUi ->
                    RepositoryCard(userRepositoryUi)
                    HorizontalDivider()
                }
                pagingUiState.show(
                    this,
                    onScrolledToEnd = {
                        mainActions.loadMore(
                            isLoadMore = isLoadMore,
                            currentRepoList = result,
                            page = page,
                        )
                    })
            }
        }
    }

    @CommonParcelize
    object Loading : MainUiState {
        @Composable
        override fun Show(mainActions: MainActions) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(progressIndicatorLarge))
            }
        }
    }

    @CommonParcelize
    object EmptyResult : MainUiState {
        @Composable
        override fun Show(mainActions: MainActions) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.nothing_image),
                    contentDescription = null,
                    modifier = Modifier.testTag(stringResource(Res.string.empty_icon_test_tag))
                )
                Text(
                    stringResource(Res.string.nothing_to_see_here),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizeXXL,
                    modifier = Modifier.testTag(stringResource(Res.string.empty_result_text_test_tag))
                )
            }
        }
    }

    @CommonParcelize
    data class Failure(val message: String) : MainUiState {
        @Composable
        override fun Show(mainActions: MainActions) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    message,
                    fontSize = fontSizeS,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.testTag(stringResource(Res.string.main_error_message_test_tag))
                )
                Button(
                    onClick = mainActions::retry,
                    modifier = Modifier.testTag(stringResource(Res.string.main_retry_button_test_tag))
                ) {
                    Text(stringResource(Res.string.retry))
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainFailurePreview() {
    MainUiState.Failure("error text example").Show(mainActions)
}

@Composable
@Preview(showSystemUi = true)
private fun MainLoadingPreview() {
    MainUiState.Loading.Show(mainActions)
}

@Composable
@Preview(showSystemUi = true)
private fun MainSuccessPreview() {
    MainUiState.Success(
        result = MockData.mockedUserRepositoriesUi,
        page = 1,
        isLoadMore = true,
        pagingUiState = PagingUiState.Loading,
    ).Show(mainActions)
}

@Composable
@Preview(showSystemUi = true)
private fun MainSuccessWithPagingFailurePreview() {
    MainUiState.Success(
        result = MockData.mockedUserRepositoriesUi,
        page = 1,
        isLoadMore = true,
        pagingUiState = PagingUiState.Failure("something went wrong"),
    ).Show(mainActions)
}


@Composable
@Preview(showSystemUi = true)
private fun MainEmptyResultPreview() {
    MainUiState.EmptyResult.Show(mainActions)
}


private val mainActions = object : MainActions {
    override fun loadUserRepo() = Unit

    override fun query(userQuery: String) = Unit


    override fun retry() = Unit
    override fun loadMore(
        isLoadMore: Boolean,
        currentRepoList: List<UserRepositoryUi>,
        page: Int
    ) = Unit

}

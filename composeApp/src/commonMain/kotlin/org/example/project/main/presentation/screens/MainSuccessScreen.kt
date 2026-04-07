package org.example.project.main.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.main_lazy_column_test_tag
import org.example.project.MockData
import org.example.project.main.presentation.MainActions
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.PagingUiState
import org.example.project.main.presentation.UserRepositoryUi
import org.example.project.main.presentation.components.RepositoryCard
import org.example.project.main.presentation.components.pagingFooter
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainSuccessScreen(
    mainUiState: MainUiState.Success,
    actions: MainActions,
) {
    LazyColumn(modifier = Modifier.testTag(stringResource(Res.string.main_lazy_column_test_tag))) {
        items(items = mainUiState.result, key = { it.id }) { userRepositoryUi ->
            RepositoryCard(
                userRepositoryUi,
                onCardClick = { repoOwner, repoName -> actions.onDetails(repoOwner, repoName) })
            HorizontalDivider()
        }
        pagingFooter(mainActions = actions, mainUiState = mainUiState)
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainSuccessPreview() {
    MainSuccessScreen(
        mainUiState = MainUiState.Success(
            result = MockData.mockedUserRepositoriesUi,
            page = 1,

            pagingUiState = PagingUiState.Loading,
        ), actions = mainPreviewActions
    )
}

@Composable
@Preview(showSystemUi = true)
private fun MainSuccessWithPagingFailurePreview() {
    MainSuccessScreen(
        mainUiState = MainUiState.Success(
            result = MockData.mockedUserRepositoriesUi,
            page = 1,

            pagingUiState = PagingUiState.Failure("failure message"),
        ),
        actions = mainPreviewActions,
    )
}

private val mainPreviewActions = object : MainActions {
    override fun query(userQuery: String) = Unit
    override fun retry() = Unit
    override fun loadMore(
        currentRepoList: List<UserRepositoryUi>,
        page: Int
    ) = Unit

    override fun refresh() = Unit
    override fun onDetails(repoOwner: String, repoName: String) = Unit

}

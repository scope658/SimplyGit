package org.example.project.main.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.search
import ktshwnumbertwo.composeapp.generated.resources.search_text_field_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.search_text_field_test_tag
import org.example.project.MockData
import org.example.project.core.GeneralFailureScreen
import org.example.project.core.GeneralLoadingIndicator
import org.example.project.main.presentation.screens.MainEmptyResultScreen
import org.example.project.main.presentation.screens.MainSuccessScreen
import org.jetbrains.compose.resources.stringResource
import theme.searchTextFieldShape
import theme.spacingL

@Composable
fun MainUi(
    isRefreshing: Boolean,
    mainUiState: MainUiState,
    searchText: String,
    mainActions: MainActions,
    onProfileClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = searchText,
                label = {
                    Text(
                        text = stringResource(Res.string.search),
                        modifier = Modifier.testTag(stringResource(Res.string.search_text_field_label_test_tag))
                    )
                },
                onValueChange = mainActions::query,
                modifier = Modifier
                    .padding(spacingL)
                    .testTag(stringResource(Res.string.search_text_field_test_tag)),
                shape = searchTextFieldShape,
                singleLine = true,
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "profile",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onProfileClick() }
                    .size(45.dp).testTag("profile_icon")

            )
        }
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                mainActions.refresh()
            }) {
            when (val state = mainUiState) {
                is MainUiState.Loading -> GeneralLoadingIndicator()
                is MainUiState.EmptyResult -> MainEmptyResultScreen()
                is MainUiState.Failure -> GeneralFailureScreen(
                    message = state.message,
                    retryAction = { mainActions.retry() }
                )

                is MainUiState.Success -> MainSuccessScreen(
                    state,
                    actions = mainActions,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun MainUiPreview() {
    val mainActions = object : MainActions {

        override fun query(userQuery: String) = Unit

        override fun retry() = Unit
        override fun loadMore(
            currentRepoList: List<UserRepositoryUi>,
            page: Int
        ) = Unit

        override fun refresh() = Unit

    }
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MainUi(
                mainUiState = MainUiState.Success(
                    result = MockData.mockedSearchRepositoriesUi,
                    page = 1,
                    isLoadMore = true,
                    pagingUiState = PagingUiState.Loading,
                ),
                searchText = "example search text",
                mainActions = mainActions,
                onProfileClick = {},
                isRefreshing = false,
            )
        }
    }
}

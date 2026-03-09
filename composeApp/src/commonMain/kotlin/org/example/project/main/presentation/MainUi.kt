package org.example.project.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.main_lazy_column_test_tag
import org.example.project.MockData
import org.example.project.main.presentation.components.RepositoryCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainUi(listRepository: List<UserRepositoryUi>) {
    LazyColumn(modifier = Modifier.testTag(stringResource(Res.string.main_lazy_column_test_tag))) {
        items(items = listRepository, key = { it.id }) { userRepositoryUi ->
            RepositoryCard(userRepositoryUi)
            HorizontalDivider()
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun MainUiPreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MainUi(
                listRepository = MockData.mockedUserRepositoriesUi
            )
        }
    }
}

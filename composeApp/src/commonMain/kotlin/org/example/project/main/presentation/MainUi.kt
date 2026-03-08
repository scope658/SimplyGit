package org.example.project.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.search
import ktshwnumbertwo.composeapp.generated.resources.search_text_field_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.search_text_field_test_tag
import org.example.project.MockData
import org.jetbrains.compose.resources.stringResource
import theme.searchTextFieldShape
import theme.spacingL

@Composable
fun MainUi(mainUiState: MainUiState, searchText: String, mainActions: MainActions) {
    Column {
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
                .fillMaxWidth()
                .padding(spacingL)
                .testTag(stringResource(Res.string.search_text_field_test_tag)),
            shape = searchTextFieldShape,
            singleLine = true,
        )
        mainUiState.Show(mainActions)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun MainUiPreview() {
    val mainActions = object : MainActions {
        override fun loadUserRepo() = Unit


        override fun query(userQuery: String) = Unit

        override fun retry() = Unit

    }
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MainUi(
                mainUiState = MainUiState.Success(result = MockData.mockedSearchRepositoriesUi),
                "example search text",
                mainActions
            )
        }
    }
}

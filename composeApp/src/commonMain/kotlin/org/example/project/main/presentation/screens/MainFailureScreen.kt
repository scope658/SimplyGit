package org.example.project.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.main_error_message_test_tag
import ktshwnumbertwo.composeapp.generated.resources.main_retry_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import theme.fontSizeS

@Composable
fun MainFailureScreen(message: String, onRetryClick: () -> Unit) {
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
            onClick = onRetryClick,
            modifier = Modifier.testTag(stringResource(Res.string.main_retry_button_test_tag))
        ) {
            Text(stringResource(Res.string.retry))
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainFailurePreview() {
    MainFailureScreen("error message", onRetryClick = {})
}

package org.example.project.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.abstract_error_message_test_tag
import ktshwnumbertwo.composeapp.generated.resources.abstract_loading_indicator_test_tag
import ktshwnumbertwo.composeapp.generated.resources.abstract_retry_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import theme.fontSizeS
import theme.progressIndicatorLarge

abstract class AbstractScreen {

    @Composable
    protected fun LoadingIndicator() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(progressIndicatorLarge)
                    .testTag(tag = stringResource(Res.string.abstract_loading_indicator_test_tag))
            )
        }
    }


    @Composable
    protected fun FailureScreen(message: String, retryAction: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                message,
                fontSize = fontSizeS,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.testTag(stringResource(Res.string.abstract_error_message_test_tag))
            )
            Button(
                onClick = retryAction,
                modifier = Modifier.testTag(stringResource(Res.string.abstract_retry_button_test_tag))
            ) {
                Text(stringResource(Res.string.retry))
            }
        }
    }
}

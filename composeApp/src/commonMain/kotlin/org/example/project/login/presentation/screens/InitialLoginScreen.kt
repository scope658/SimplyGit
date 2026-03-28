package org.example.project.login.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.app_name
import ktshwnumbertwo.composeapp.generated.resources.app_name_test_tag
import ktshwnumbertwo.composeapp.generated.resources.github_icon
import ktshwnumbertwo.composeapp.generated.resources.login_error_message_test_tag
import ktshwnumbertwo.composeapp.generated.resources.login_poster_test_tag
import ktshwnumbertwo.composeapp.generated.resources.sign_in
import ktshwnumbertwo.composeapp.generated.resources.sign_in_button_test_tag
import org.example.project.login.presentation.ErrorState
import org.example.project.login.presentation.LoginActions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.buttonHeightLarge
import theme.darkIndigo
import theme.fontSizeS
import theme.loginInButtonShape
import theme.loginLogoSize
import theme.spacingM
import theme.spacingXL

@Composable
fun InitialLoginScreen(errorState: ErrorState, actions: LoginActions) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.github_icon),
            contentDescription = null,
            modifier = Modifier
                .size(loginLogoSize)
                .testTag(stringResource(Res.string.login_poster_test_tag))
        )

        Spacer(modifier = Modifier.height(spacingM))

        Text(
            text = stringResource(Res.string.app_name),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = darkIndigo,
            modifier = Modifier.testTag(stringResource(Res.string.app_name_test_tag))
        )

        Spacer(modifier = Modifier.height(spacingM))

        Button(
            onClick = actions::loginIn,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(buttonHeightLarge)
                .testTag(stringResource(Res.string.sign_in_button_test_tag)),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkIndigo,
                contentColor = Color.White
            ),
            shape = loginInButtonShape
        ) {
            Text(
                text = stringResource(Res.string.sign_in),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(spacingM))
        when (errorState) {
            is ErrorState.Empty -> Unit
            is ErrorState.Error -> {
                Text(
                    text = errorState.message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = fontSizeS,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag(stringResource(Res.string.login_error_message_test_tag))
                        .padding(horizontal = spacingXL),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginErrorUiStatePreview() {
    InitialLoginScreen(ErrorState.Error("auth failed"), actions = loginPreviewActions)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginUiStatePreview() {
    InitialLoginScreen(errorState = ErrorState.Empty, loginPreviewActions)
}

private val loginPreviewActions = object : LoginActions {

    override fun loginIn() = Unit
}

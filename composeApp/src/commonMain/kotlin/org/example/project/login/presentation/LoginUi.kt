package org.example.project.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.login_label
import ktshwnumbertwo.composeapp.generated.resources.login_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.login_text_field_test_tag
import ktshwnumbertwo.composeapp.generated.resources.password_label
import ktshwnumbertwo.composeapp.generated.resources.password_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.password_text_field_test_tag
import ktshwnumbertwo.composeapp.generated.resources.sign_in
import ktshwnumbertwo.composeapp.generated.resources.sign_in_button_test_tag
import org.example.project.login.presentation.components.AuthTextField
import org.example.project.login.presentation.components.ErrorState
import org.example.project.login.presentation.components.TrailingIconState
import org.jetbrains.compose.resources.stringResource
import theme.spacingM


@Composable
fun LoginUi(loginUiState: LoginUiState, loginActions: LoginActions) {

    val isError = loginUiState.error is ErrorState.Error

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AuthTextField(
            labelText = stringResource(Res.string.login_label),
            value = loginUiState.login,
            onValueChange = loginActions::onUsernameChanged,
            isError = isError,
            textFieldModifier = Modifier.testTag(stringResource(Res.string.login_text_field_test_tag)),
            labelModifier = Modifier.testTag(stringResource(Res.string.login_label_test_tag)),
            trailingIconState = TrailingIconState.Login,
            onTrailingIconClick = {},
        )
        Spacer(modifier = Modifier.height(spacingM))
        AuthTextField(
            labelText = stringResource(Res.string.password_label),
            value = loginUiState.password,
            onValueChange = loginActions::onPasswordChanged,
            isError = isError,
            errorText = loginUiState.error,
            textFieldModifier = Modifier.testTag(stringResource(Res.string.password_text_field_test_tag)),
            labelModifier = Modifier.testTag(stringResource(Res.string.password_label_test_tag)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformationState = loginUiState.visualTransformationState,
            trailingIconState = loginUiState.trailingIconState,
            onTrailingIconClick = loginActions::changePasswordVisibility,

        )
        Spacer(modifier = Modifier.height(spacingM))
        Button(
            enabled = loginUiState.isLoginButtonActive,
            onClick = loginActions::login,
            modifier = Modifier.testTag(stringResource(Res.string.sign_in_button_test_tag)),
        ) {
            Text(text = stringResource(Res.string.sign_in))
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun LoginUiPreview() {
    LoginUi(
        loginUiState = previewLoginUiState,
        loginActions = loginPreviewActions,
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun LoginErrorPreview() {
    LoginUi(
        loginUiState = previewLoginUiState.copy(
            isLoginButtonActive = true,
            error = ErrorState.Error
        ),
        loginActions = loginPreviewActions
    )
}

private val loginPreviewActions = object : LoginActions {

    override fun onUsernameChanged(userName: String) = Unit

    override fun onPasswordChanged(password: String) = Unit

    override fun login() = Unit

    override fun changePasswordVisibility() = Unit
}

private val previewLoginUiState = LoginUiState.INITIAL.copy(login = "admin", password = "admin1234")




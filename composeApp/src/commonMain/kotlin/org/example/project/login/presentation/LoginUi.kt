package org.example.project.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.invalid_login_or_password
import ktshwnumbertwo.composeapp.generated.resources.login_label
import ktshwnumbertwo.composeapp.generated.resources.password_label
import ktshwnumbertwo.composeapp.generated.resources.sign_in
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize
import org.jetbrains.compose.resources.stringResource


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
            textFieldModifier = Modifier.testTag("login_text_field"),
            labelModifier = Modifier.testTag("login_label"),
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(
            labelText = stringResource(Res.string.password_label),
            value = loginUiState.password,
            onValueChange = loginActions::onPasswordChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = isError,
            errorText = loginUiState.error,
            textFieldModifier = Modifier.testTag("password_text_field"),
            labelModifier = Modifier.testTag("password_label"),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = loginActions::login,
            modifier = Modifier.testTag("sign_in_button"),
            enabled = loginUiState.isLoginButtonActive
        ) {
            Text(text = stringResource(Res.string.sign_in))
        }
    }
}

@Composable
fun AuthTextField(
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorText: ErrorState = ErrorState.Empty,
    isError: Boolean,
    textFieldModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = textFieldModifier.fillMaxWidth().padding(horizontal = 40.dp),
        label = { Text(text = labelText, modifier = labelModifier) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        isError = isError,
        supportingText = { errorText.Show() }
    )
}


interface ErrorState : CommonParcelable {
    @Composable
    fun Show()

    @CommonParcelize
    object Empty : ErrorState {
        @Composable
        override fun Show() = Unit
    }

    @CommonParcelize
    object Error : ErrorState {
        @Composable
        override fun Show() {
            Text(
                stringResource(Res.string.invalid_login_or_password),
                modifier = Modifier.testTag("password_supp_text")
            )
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
            isLoginButtonActive = false,
            error = ErrorState.Error
        ),
        loginActions = loginPreviewActions
    )
}

private val loginPreviewActions = object : LoginActions {
    override fun onUsernameChanged(userName: String) = Unit

    override fun onPasswordChanged(password: String) = Unit

    override fun login() = Unit
}

private val previewLoginUiState = LoginUiState(
    login = "admin", password = "admin1234", isLoginButtonActive = true, error = ErrorState.Empty
)



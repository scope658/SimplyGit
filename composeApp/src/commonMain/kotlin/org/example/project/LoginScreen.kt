package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.login_label
import ktshwnumbertwo.composeapp.generated.resources.login_text_field_test_tag
import ktshwnumbertwo.composeapp.generated.resources.password_label
import ktshwnumbertwo.composeapp.generated.resources.password_text_field_test_tag
import ktshwnumbertwo.composeapp.generated.resources.sign_in
import ktshwnumbertwo.composeapp.generated.resources.sign_in_button_test_tag
import org.example.project.components.AuthTextField
import org.jetbrains.compose.resources.stringResource
import theme.spacingStandard


@Composable
fun LoginScreen() {

    var loginField by rememberSaveable { mutableStateOf("") }
    var passwordField by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AuthTextField(
            labelText = stringResource(Res.string.login_label),
            value = loginField,
            onValueChange = { loginField = it },
            modifier = Modifier.testTag(stringResource(Res.string.login_text_field_test_tag)),
        )
        Spacer(modifier = Modifier.height(spacingStandard))
        AuthTextField(
            labelText = stringResource(Res.string.password_label),
            value = passwordField,
            onValueChange = { passwordField = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.testTag(stringResource(Res.string.password_text_field_test_tag))
        )
        Spacer(modifier = Modifier.height(spacingStandard))
        Button(
            onClick = {},
            modifier = Modifier.testTag(stringResource(Res.string.sign_in_button_test_tag))
        ) {
            Text(text = stringResource(Res.string.sign_in))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}

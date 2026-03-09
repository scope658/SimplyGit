package org.example.project.login.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.auth_icon_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.hide_password
import ktshwnumbertwo.composeapp.generated.resources.hide_password_icon_test_tag
import ktshwnumbertwo.composeapp.generated.resources.invalid_login_or_password
import ktshwnumbertwo.composeapp.generated.resources.password_supp_text_test_tag
import ktshwnumbertwo.composeapp.generated.resources.show_password
import ktshwnumbertwo.composeapp.generated.resources.show_password_icon_test_tag
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize
import org.jetbrains.compose.resources.stringResource
import theme.authTextFieldShape
import theme.spacingXXL


@Composable
fun AuthTextField(
    labelText: String,
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit,
    errorText: ErrorState = ErrorState.Empty,
    trailingIconState: TrailingIconState = TrailingIconState.Login,
    visualTransformationState: VisualTransformationState = VisualTransformationState.Show,
    textFieldModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,

    ) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = textFieldModifier.fillMaxWidth()
            .padding(horizontal = spacingXXL),
        label = { Text(text = labelText, modifier = labelModifier) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformationState.State(),
        isError = isError,
        supportingText = { errorText.Show() },
        shape = authTextFieldShape,
        trailingIcon = {
            trailingIconState.Show(onTrailingIconClick)
        }
    )
}


interface VisualTransformationState : CommonParcelable {

    @Composable
    fun State(): VisualTransformation

    @CommonParcelize
    object Show : VisualTransformationState {
        @Composable
        override fun State(): VisualTransformation = VisualTransformation.None

    }

    @CommonParcelize
    object Hide : VisualTransformationState {
        @Composable
        override fun State(): VisualTransformation = PasswordVisualTransformation()
    }
}


interface TrailingIconState : CommonParcelable {

    @Composable
    fun Show(onClick: () -> Unit)

    @CommonParcelize
    object Login : TrailingIconState {
        @Composable
        override fun Show(onClick: () -> Unit) = Unit
    }

    @CommonParcelize
    data class Password(val visibilityIconState: VisibilityIconState) : TrailingIconState {
        @Composable
        override fun Show(
            onClick: () -> Unit,
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.testTag(stringResource(Res.string.auth_icon_button_test_tag))
            ) {
                visibilityIconState.Apply()
            }
        }
    }
}

interface VisibilityIconState : CommonParcelable {

    @Composable
    fun Apply()


    @CommonParcelize
    object Show : VisibilityIconState {
        @Composable
        override fun Apply() {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = stringResource(Res.string.show_password),
                modifier = Modifier.testTag(
                    stringResource(Res.string.show_password_icon_test_tag)
                )
            )
        }
    }

    @CommonParcelize
    object Hide : VisibilityIconState {
        @Composable
        override fun Apply() {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = stringResource(Res.string.hide_password),
                modifier = Modifier.testTag(stringResource(Res.string.hide_password_icon_test_tag)),

                )
        }
    }
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
                modifier = Modifier.testTag(stringResource(Res.string.password_supp_text_test_tag))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginTextFieldPreview() {
    AuthTextField(
        "Login",
        "admin",
        true,
        errorText = ErrorState.Error,
        onValueChange = {},
        onTrailingIconClick = {},
    )
}

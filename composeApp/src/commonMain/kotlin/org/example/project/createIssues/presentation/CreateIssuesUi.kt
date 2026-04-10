package org.example.project.createIssues.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.cancel_button
import ktshwnumbertwo.composeapp.generated.resources.create_button
import ktshwnumbertwo.composeapp.generated.resources.create_issue_title
import ktshwnumbertwo.composeapp.generated.resources.create_issues_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issue_description_label
import ktshwnumbertwo.composeapp.generated.resources.issue_title_label
import ktshwnumbertwo.composeapp.generated.resources.issues_cancel_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_desc_text_field_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_desc_text_field_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_loading_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_title_text_field_label_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_title_text_field_test_tag
import org.jetbrains.compose.resources.stringResource
import theme.height150
import theme.height48
import theme.spacingL
import theme.spacingS
import theme.spacingXL
import theme.spacingXXXL
import theme.stroke2dp

@Composable
fun CreateIssueUi(createIssueScreenState: IssueScreenState, createIssuesActions: IssuesActions) =
    with(createIssueScreenState) {
        val isBodyError = bodySuppText.isNotBlank()
        val isTitleError = titleSuppText.isNotBlank()
        Scaffold(
            modifier = Modifier.padding(spacingL)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingS)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(spacingXXXL)
                    )
                    Text(
                        text = stringResource(Res.string.create_issue_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { createIssuesActions.onTitleChanged(it) },
                    label = {
                        Text(
                            text = stringResource(Res.string.issue_title_label),
                            modifier = Modifier.testTag(stringResource(Res.string.issues_title_text_field_label_test_tag))
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(Res.string.issues_title_text_field_test_tag)),
                    isError = isTitleError,
                    supportingText = {
                        if (isTitleError) {
                            Text(text = titleSuppText)
                        }
                    },
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(spacingL))

                OutlinedTextField(
                    value = body,
                    onValueChange = { createIssuesActions.onBodyChanged(it) },
                    label = {
                        Text(
                            text = stringResource(Res.string.issue_description_label),
                            modifier = Modifier.testTag(stringResource(Res.string.issues_desc_text_field_label_test_tag))
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height150)
                        .testTag(stringResource(Res.string.issues_desc_text_field_test_tag)),
                    maxLines = 5,
                    isError = isBodyError,
                    supportingText = {
                        if (isBodyError) {
                            Text(text = bodySuppText)
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { createIssuesActions.create() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(Res.string.create_issues_button_test_tag))
                        .height(height48),
                    enabled = isCreateButtonActive,
                ) {
                    when (val state = buttonState) {
                        is ButtonState.Failure -> Text(
                            state.message, style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )

                        is ButtonState.Init -> Text(stringResource(Res.string.create_button))
                        is ButtonState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.size(spacingXL)
                                .testTag(stringResource(Res.string.issues_loading_test_tag)),
                            strokeWidth = stroke2dp,
                        )
                    }
                }
                TextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                        .testTag(stringResource(Res.string.issues_cancel_button_test_tag))
                ) {
                    Text(stringResource(Res.string.cancel_button))
                }
            }
        }

    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateIssuePreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            CreateIssueUi(
                createIssueScreenState = createIssueScreenState,
                createIssuesActions = object : IssuesActions {
                    override fun create() = Unit


                    override fun onTitleChanged(title: String) = Unit

                    override fun onBodyChanged(body: String) = Unit

                }
            )
        }
    }
}

private val createIssueScreenState = IssueScreenState(
    title = "fake title",
    titleSuppText = "fake title supp text",
    body = "fake body",
    bodySuppText = "fake body supp text",
    isCreateButtonActive = false,
    isSuccess = false,
    buttonState = ButtonState.Failure(" no connection")
)

package org.example.project.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_image_test_tag
import ktshwnumbertwo.composeapp.generated.resources.onboarding_next
import ktshwnumbertwo.composeapp.generated.resources.onboarding_skip
import ktshwnumbertwo.composeapp.generated.resources.onboarding_skip_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.onboarding_text_test_tag
import ktshwnumbertwo.composeapp.generated.resources.onboarding_title_test_tag
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.fontSizeS
import theme.fontSizeXXL
import theme.spacingHuge
import theme.spacingL
import theme.spacingM
import theme.spacingXL
import theme.spacingXXL

@Composable
fun OnboardingUi(onboardingPage: OnboardingPage, onboardingActions: OnboardingActions) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(onboardingPage.image),
            contentDescription = null,
            modifier = Modifier.padding(spacingM)
                .testTag(stringResource(Res.string.onboarding_image_test_tag))
                .clearAndSetSemantics {
                    this.DrawableRes = onboardingPage.image
                }
        )

        Spacer(modifier = Modifier.height(spacingHuge))
        Text(
            text = stringResource(onboardingPage.title),
            fontSize = fontSizeXXL,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(stringResource(Res.string.onboarding_title_test_tag))
        )
        Spacer(modifier = Modifier.height(spacingXL))
        Text(
            text = stringResource(onboardingPage.description),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontSize = fontSizeS,
            modifier = Modifier.testTag(
                stringResource(Res.string.onboarding_text_test_tag)
            ),
        )
        Spacer(modifier = Modifier.height(spacingXXL))
        FilledTonalButton(
            onClick = onboardingActions::nextPage,
            modifier = Modifier.testTag(stringResource(Res.string.onboarding_button_test_tag))
        ) {
            Text(
                text = stringResource(Res.string.onboarding_next),
                modifier = Modifier.padding(spacingL)
            )
        }
        TextButton(
            onClick = onboardingActions::skipOnboarding,
            modifier = Modifier.testTag(stringResource(Res.string.onboarding_skip_button_test_tag))
        ) {
            Text(text = stringResource(Res.string.onboarding_skip))
        }
    }
}

val DrawableRes = SemanticsPropertyKey<DrawableResource>(name = "DrawableRes")
var SemanticsPropertyReceiver.DrawableRes by DrawableRes


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnboardingUiPreview() {
    OnboardingUi(
        onboardingPage = OnboardingPage(
            Res.string.onboarding_first_title,
            Res.string.onboarding_first_desc,
            Res.drawable.first_onboarding_image
        ),
        onboardingActions = object : OnboardingActions {

            override fun nextPage() = Unit

            override fun skipOnboarding() = Unit

            override fun finishOnboarding() = Unit
        })
}

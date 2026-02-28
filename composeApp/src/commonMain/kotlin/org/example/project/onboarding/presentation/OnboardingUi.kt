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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.onboarding_next
import ktshwnumbertwo.composeapp.generated.resources.onboarding_skip
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
            modifier = Modifier.padding(10.dp).testTag("onboarding_image")
                .semantics {
                    this.DrawableRes = onboardingPage.image
                }
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(onboardingPage.title),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("onboarding_title")
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(onboardingPage.description),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            modifier = Modifier.testTag(
                "onboarding_text"
            ),
        )
        Spacer(modifier = Modifier.height(40.dp))
        FilledTonalButton(
            onClick = onboardingActions::nextPage,
            modifier = Modifier.testTag("onboarding_button")
        ) {
            Text(
                text = stringResource(Res.string.onboarding_next),
                modifier = Modifier.padding(16.dp)
            )
        }
        TextButton(
            onClick = onboardingActions::skipOnboarding,
            modifier = Modifier.testTag("skip_onboarding_button")
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
        onboardingPage = OnboardingStepState.FirstPage.currentState(),
        onboardingActions = object : OnboardingActions {

            override fun nextPage() = Unit

            override fun skipOnboarding() = Unit

            override fun finishOnboarding() = Unit
        })
}
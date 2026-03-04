package org.example.project

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.get_started
import ktshwnumbertwo.composeapp.generated.resources.mock_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.onboarding_description
import ktshwnumbertwo.composeapp.generated.resources.onboarding_text_test_tag
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.onboardingImageShape
import theme.paddingMedium
import theme.spacingExtraLarge

@Composable
fun OnboardingScreen(onClick: () -> Unit) {
    val mockOnboardingImageRes = Res.drawable.mock_onboarding_image
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = AppAssets.imageUrl,
            contentDescription = null,
            modifier = Modifier.clip(shape = onboardingImageShape)
                .testTag(AppAssets.imageUrl),
            placeholder = painterResource(resource = mockOnboardingImageRes),
            contentScale = ContentScale.Crop,
            error = painterResource(mockOnboardingImageRes)
        )
        Spacer(modifier = Modifier.height(spacingExtraLarge))
        Text(
            text = stringResource(Res.string.onboarding_description),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W300,
            fontSize = 25.sp,
            modifier = Modifier.testTag(
                stringResource(Res.string.onboarding_text_test_tag)
            )
        )
        Spacer(modifier = Modifier.height(spacingExtraLarge))
        FilledTonalButton(
            onClick = { onClick() },
            modifier = Modifier.testTag(stringResource(Res.string.onboarding_button_test_tag))
        ) {
            Text(
                text = stringResource(Res.string.get_started), modifier = Modifier.padding(
                    paddingMedium
                )
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnboardingPreview() {
    OnboardingScreen(onClick = {})
}

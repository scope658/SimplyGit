import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.example.project.onboarding.presentation.DrawableRes
import org.jetbrains.compose.resources.DrawableResource


class OnboardingPage(composeTestRule: ComposeTestRule) {

    private val image = composeTestRule.onNodeWithTag("onboarding_image")
    private val onboardingText = composeTestRule.onNodeWithTag("onboarding_text")
    private val onboardingTitle = composeTestRule.onNodeWithTag("onboarding_title")
    private val continueButton = composeTestRule.onNodeWithTag("onboarding_button")
    private val skipButton = composeTestRule.onNodeWithTag("skip_onboarding_button")

    fun checkVisibleNow(
        imageRes: DrawableResource,
        onboardingTitle: String,
        onboardingText: String
    ) {

        image.assertIsDisplayed()
            .assert(
                matcher = SemanticsMatcher.expectValue(
                    key = DrawableRes,
                    expectedValue = imageRes,
                )
            )

        this.onboardingTitle
            .assertIsDisplayed()
            .assertTextEquals(onboardingTitle)

        this.onboardingText
            .assertIsDisplayed()
            .assertTextEquals(
                onboardingText
            )

        continueButton
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertTextEquals("Next")

        skipButton
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertTextEquals("Skip")
    }

    fun clickContinueButton() {
        continueButton.performClick()
    }

    fun clickSkipButton() {
        skipButton.performClick()
    }

    fun checkNotVisibleNow() {
        image.assertDoesNotExist()
        onboardingTitle.assertDoesNotExist()
        onboardingText.assertDoesNotExist()
        continueButton.assertDoesNotExist()
        skipButton.assertDoesNotExist()
    }
}
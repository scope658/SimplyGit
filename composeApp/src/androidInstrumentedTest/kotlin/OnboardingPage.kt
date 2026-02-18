import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.compose_multiplatform


class OnboardingPage(composeTestRule: ComposeTestRule) {

    private val image = composeTestRule.onNodeWithTag("onboarding_image")
    private val text = composeTestRule.onNodeWithTag("onboarding_text")
    private val button = composeTestRule.onNodeWithTag("onboarding_button")

    fun checkVisibleNow() {
        image.assertIsDisplayed()
            .assert(
                matcher = SemanticsMatcher.expectValue(
                    key = DrawableRes,
                    expectedValue = Res.drawable.mock_onboarding_image,
                )
            )
        text.assertIsDisplayed()
            .assertTextEquals(
                "Explore thousands of cat photos from around the world. " +
                        "Find your favorite feline friends!"
            )

        button.assertIsDisplayed()
            .assertHasClickAction()
            .assertTextEquals("Get Started")
    }

    fun clickContinueButton() {
        button.performClick()
    }

    fun checkNotVisibleNow() {
        image.assertIsNotDisplayed()
        text.assertIsNotDisplayed()
        button.assertIsNotDisplayed()
    }
}
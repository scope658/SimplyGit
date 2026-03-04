import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


class OnboardingPage(composeTestRule: ComposeTestRule) {

    private val image = composeTestRule.onNodeWithTag(FAKE_IMAGE_URL)
    private val text = composeTestRule.onNodeWithTag("onboarding_text")
    private val button = composeTestRule.onNodeWithTag("onboarding_button")

    fun checkVisibleNow() {
        image
            .assertIsDisplayed()

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
        image.assertDoesNotExist()
        text.assertDoesNotExist()
        button.assertDoesNotExist()
    }

    companion object {
        private const val FAKE_IMAGE_URL = "fakeImageUrl"
    }
}

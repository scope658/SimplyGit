package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


class LoginPage(private val composeTestRule: ComposeTestRule) {

    private val loginPoster = composeTestRule.onNodeWithTag("login_poster")
    private val signInButton = composeTestRule.onNodeWithTag("sign_in_button")
    private val appName = composeTestRule.onNodeWithTag("app_name")
    private val errorMessage = composeTestRule.onNodeWithTag("error_message")


    fun checkVisibleNow() {
        loginPoster
            .assertIsDisplayed()

        signInButton
            .assertIsDisplayed()
            .assertTextContains("Sign in with GitHub")
            .assertHasClickAction()
            .assertIsEnabled()

        appName
            .assertIsDisplayed()
            .assertTextEquals("SimplyGit")

        errorMessage
            .assertDoesNotExist()

    }

    fun clickSignInButton() {
        signInButton
            .performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun checkErrorMessageIsVisible(errorMessage: String) {
        composeTestRule.waitUntilExactlyOneExists(
            matcher = hasText(errorMessage),
            timeoutMillis = 5000
        )

        this.errorMessage
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)

    }
}

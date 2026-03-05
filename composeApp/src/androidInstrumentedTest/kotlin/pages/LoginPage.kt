package pages

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


class LoginPage(composeTestRule: ComposeTestRule) {

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
            .assertIsNotEnabled()

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

    fun checkErrorMessageIsVisible(errorMessage: String) {
        this.errorMessage
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)

    }
}

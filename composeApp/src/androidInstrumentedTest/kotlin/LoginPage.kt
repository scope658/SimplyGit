import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput


class LoginPage(composeTestRule: ComposeTestRule) {

    private val loginTextField = composeTestRule.onNodeWithTag("login_text_field")
    private val loginLabel = composeTestRule.onNodeWithTag("login_label", useUnmergedTree = true)
    private val passwordTextField = composeTestRule.onNodeWithTag("password_text_field")
    private val passwordLabel =
        composeTestRule.onNodeWithTag("password_label", useUnmergedTree = true)
    private val passwordSuppText =
        composeTestRule.onNodeWithTag("password_supp_text", useUnmergedTree = true)
    private val signInButton = composeTestRule.onNodeWithTag("sign_in_button")

    fun checkVisibleNow() {
        loginTextField
            .assertIsDisplayed()

        loginLabel
            .assertIsDisplayed()
            .assertTextEquals("Login")

        passwordTextField
            .assertIsDisplayed()

        passwordLabel
            .assertIsDisplayed()
            .assertTextEquals("Password")

        passwordSuppText
            .assertIsNotDisplayed()

        signInButton
            .assertIsDisplayed()
            .assertTextContains("Sign in")
            .assertHasClickAction()
            .assertIsNotEnabled()
    }

    fun typeLogin(login: String) {
        loginTextField.performTextInput(text = login)
    }

    fun typePassword(password: String) {
        passwordTextField.performTextInput(text = password)
    }

    fun clickSignInButton() {
        signInButton
            .performClick()
    }

    fun checkInputValues(loginField: String, passwordField: String) {
        loginTextField
            .assert(hasText(loginField))
        passwordTextField
            .assert(hasText(passwordField))
    }

    fun checkButtonIsActive() {
        signInButton
            .assertIsEnabled()
    }

    fun checkButtonIsNotActive() {
        signInButton
            .assertIsNotEnabled()
    }

    fun checkInputsIsNotValid() {
        passwordSuppText
            .assertIsDisplayed()
            .assertTextEquals("Invalid login or password")
    }
}
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput


class LoginPage(composeTestRule: ComposeTestRule) {

    private val loginTextField = composeTestRule.onNodeWithTag("login_text_field")
    private val passwordTextField = composeTestRule.onNodeWithTag("password_text_field")

    fun checkVisibleNow() {
        loginTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
    }

    fun typeLogin(login: String) {
        loginTextField.performTextInput(text = login)
    }

    fun typePassword(password: String) {
        passwordTextField.performTextInput(text = password)
    }

    fun checkInputValues(loginField: String, passwordField: String) {
        loginTextField.assert(hasText(loginField))
        passwordTextField.assert(hasText(passwordField))
    }
}
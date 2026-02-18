import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import kotlin.math.log


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
        loginTextField.performTextInput(text = password)
    }

    fun checkInputValues(loginField: String, passwordField: String) {
        loginTextField.assertTextEquals(loginField)
        passwordTextField.assertTextEquals(passwordField)
    }
}
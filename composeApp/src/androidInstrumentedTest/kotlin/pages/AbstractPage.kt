package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


@OptIn(ExperimentalTestApi::class)
abstract class AbstractPage(private val composeTestRule: ComposeTestRule) {

    protected val retryButton = composeTestRule.onNodeWithTag("abstract_retry_button")
    protected val errorMessage = composeTestRule.onNodeWithTag("abstract_error_message")

    fun waitUntilLoadingDoesNotExist() {
        composeTestRule
            .waitUntilDoesNotExist(
                matcher = hasTestTag("abstract_loading_indicator"), timeoutMillis = TIME_MILLS
            )
    }

    fun clickRetryButton() {
        retryButton
            .performClick()
    }

    fun checkErrorMessageIsVisible(message: String) {
        errorMessage
            .assertIsDisplayed()
            .assertTextEquals(message)
    }

    companion object {
        private const val TIME_MILLS = 2500L
    }
}

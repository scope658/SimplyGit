package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class CreateIssuesPage(private val composeTestRule: ComposeTestRule) {

    private val createButton = composeTestRule.onNodeWithTag("create_issues_button")

    private val titleTextField = composeTestRule.onNodeWithTag("issues_title_text_field")
    private val titleTextFieldLabel =
        composeTestRule.onNodeWithTag("issues_title_text_field_label", true)

    private val descTextField = composeTestRule.onNodeWithTag("issues_desc_text_field")
    private val descTextFieldLabel =
        composeTestRule.onNodeWithTag("issues_desc_text_field_label", true)

    private val cancelButton = composeTestRule.onNodeWithTag("issues_cancel_button")

    fun checkVisibleNow() {
        cancelButton
            .assertTextEquals("Cancel")
            .assertIsDisplayed()
            .assertIsEnabled()

        createButton
            .assertTextEquals("Create")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        descTextField
            .assertIsDisplayed()

        descTextFieldLabel
            .assertTextEquals("Description")
            .assertIsDisplayed()

        titleTextFieldLabel
            .assertTextEquals("Title")

        titleTextField
            .assertIsDisplayed()
    }

    fun checkButtonIsNotEnabled() {
        createButton
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    fun typeTitle(title: String) {
        titleTextField
            .performTextInput(title)
    }

    fun typeDesc(desc: String) {
        descTextField
            .performTextInput(desc)
    }

    fun checkButtonIsEnabled() {
        createButton
            .assertIsEnabled()
    }

    fun clickCreateButton() {
        createButton
            .performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun checkErrorMessage(message: String) {
        composeTestRule.waitUntilDoesNotExist(
            hasTestTag("issues_loading")
        )

        createButton
            .assertTextEquals(message)
    }

}

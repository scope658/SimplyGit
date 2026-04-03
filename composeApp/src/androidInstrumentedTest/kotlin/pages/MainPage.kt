package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import org.example.project.main.presentation.UserRepositoryUi

@OptIn(ExperimentalTestApi::class)
class MainPage(private val composeTestRule: ComposeTestRule) : AbstractPage(composeTestRule) {

    private val userRepositoryLazyColum = composeTestRule.onNodeWithTag("main_lazy_column")
    private val searchTextField = composeTestRule.onNodeWithTag("search_text_field")
    private val searchTextFieldLabel =
        composeTestRule.onNodeWithTag("search_text_field_label", useUnmergedTree = true)

    private val emptyResultIcon = composeTestRule.onNodeWithTag("empty_icon")
    private val emptyResultText = composeTestRule.onNodeWithTag("empty_result_text")

    fun checkVisibleNow() {

        searchTextField
            .assertIsDisplayed()

        searchTextFieldLabel
            .assertIsDisplayed()
            .assertTextEquals("Search")
    }

    fun inputQuery(query: String) {
        searchTextField
            .performTextInput(query)
        Espresso.closeSoftKeyboard()
    }

    fun checkQueryText(query: String) {
        searchTextField
            .assert(hasText(query))
    }

    fun clearInputText() {
        searchTextField
            .performTextClearance()
    }

    fun checkUserRepositories(userRepositories: List<UserRepositoryUi>) {
        userRepositories.forEach {
            assertRepositories(it)
        }
    }

    private fun assertRepositories(userRepo: UserRepositoryUi) = with(composeTestRule) {
        userRepositoryLazyColum
            .performScrollToNode(hasTestTag("user_repo_card_${userRepo.id}"))
            .assertIsDisplayed()

        onNodeWithTag("user_photo_${userRepo.id}", true)
            .assertIsDisplayed()


        onNodeWithTag("user_name_${userRepo.id}", true)
            .assertTextEquals(userRepo.userName)

        onNodeWithTag("repo_name_${userRepo.id}", true)
            .assertTextEquals(userRepo.repositoryName)

        onNodeWithTag("programming_lang_${userRepo.id}", true)
            .assertTextEquals(userRepo.programmingLanguage)

        onNodeWithTag("star_icon_${userRepo.id}", true)
            .assertIsDisplayed()

        onNodeWithTag("repo_star_${userRepo.id}", true)
            .assertTextEquals(userRepo.stars.toString())
    }


    fun checkFailureState(errorMessage: String) {
        this.errorMessage
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)
    }


    fun checkEmptyResultStateVisible() {
        emptyResultIcon
            .assertIsDisplayed()

        emptyResultText
            .assertIsDisplayed()
            .assertTextEquals("Nothing to see here")
    }

    fun clickRepo(
        repoId: Int
    ) {
        val userCardTag = "user_repo_card_${repoId}"
        userRepositoryLazyColum
            .performScrollToNode(hasTestTag(userCardTag))

        composeTestRule.onNodeWithTag(userCardTag)
            .performClick()
    }
}

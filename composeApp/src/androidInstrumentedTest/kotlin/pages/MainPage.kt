package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import org.example.project.main.presentation.UserRepositoryUi


class MainPage(private val composeTestRule: ComposeTestRule) {

    val userRepositoryLazyColum = composeTestRule.onNodeWithTag("main_lazy_column")
    val searchTextField = composeTestRule.onNodeWithTag("search_text_field")

    fun checkVisibleNow() {
        searchTextField
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun checkUserRepositories(userRepositories: List<UserRepositoryUi>) {
        composeTestRule.waitUntilExactlyOneExists(
            matcher = hasTestTag("main_lazy_column"),
            timeoutMillis = 5000
        )
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
}

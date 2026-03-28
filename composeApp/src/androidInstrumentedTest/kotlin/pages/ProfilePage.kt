package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


@OptIn(ExperimentalTestApi::class)
class ProfilePage(private val composeTestRule: ComposeTestRule) : AbstractPage(composeTestRule) {

    private val avatar = composeTestRule.onNodeWithTag("user_avatar")
    private val userName = composeTestRule.onNodeWithTag("user_name")
    private val bio = composeTestRule.onNodeWithTag("user_bio")
    private val repoCount = composeTestRule.onNodeWithTag("repo_count")
    private val subscribersCount = composeTestRule.onNodeWithTag("subscribers_count")
    private val logoutButton = composeTestRule.onNodeWithTag("logout_button")


    fun checkVisibleNow(
        userName: String,
        bio: String,
        repoCount: String,
        subscribersCount: String
    ) {

        avatar
            .assertIsDisplayed()

        this.userName
            .assertIsDisplayed()
            .assertTextEquals(userName)

        this.bio
            .assertIsDisplayed()
            .assertTextEquals(bio)

        this.repoCount
            .assertIsDisplayed()
            .assertTextEquals(repoCount)

        this.subscribersCount
            .assertIsDisplayed()
            .assertTextEquals(subscribersCount)

        this.logoutButton
            .assertTextContains("Sign Out")
            .assertIsDisplayed()

        this.errorMessage
            .assertDoesNotExist()
        this.retryButton
            .assertDoesNotExist()
    }

    fun clickLogoutButton() {
        logoutButton
            .performClick()
    }
}

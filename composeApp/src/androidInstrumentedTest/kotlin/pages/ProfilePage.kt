package pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick


@OptIn(ExperimentalTestApi::class)
class ProfilePage(private val composeTestRule: ComposeTestRule) {

    private val avatar = composeTestRule.onNodeWithTag("user_avatar")
    private val userName = composeTestRule.onNodeWithTag("user_name")
    private val bio = composeTestRule.onNodeWithTag("user_bio")
    private val repoCount = composeTestRule.onNodeWithTag("repo_count")
    private val subscribersCount = composeTestRule.onNodeWithTag("subscribers_count")
    private val logoutButton = composeTestRule.onNodeWithTag("logout_button")

    private val errorMessage = composeTestRule.onNodeWithTag("profile_error_message")
    private val retryProfileButton = composeTestRule.onNodeWithTag("retry_profile_button")


    fun checkVisibleNow(
        userName: String,
        bio: String,
        repoCount: String,
        subscribersCount: String
    ) {

        composeTestRule
            .waitUntilDoesNotExist(
                matcher = hasTestTag("profile_loading_indicator")
            )


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

        this.errorMessage
            .assertDoesNotExist()

        this.retryProfileButton
            .assertDoesNotExist()
    }

    fun clickLogoutButton() {
        logoutButton
            .performClick()
    }

    fun checkErrorVisibleNow() {
        composeTestRule
            .waitUntilDoesNotExist(
                matcher = hasTestTag("profile_loading_indicator")
            )

        errorMessage
            .assertIsDisplayed()
            .assertTextEquals("something went wrong")

        retryProfileButton
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertTextContains("Retry")

    }

    fun clickRetryButton() {
        retryProfileButton
            .performClick()

    }
}

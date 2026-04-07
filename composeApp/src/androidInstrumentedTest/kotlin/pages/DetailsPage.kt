package pages

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class DetailsPage(private val composeTestRule: ComposeTestRule) : AbstractPage(composeTestRule) {

    val repoName = composeTestRule.onNodeWithTag("repo_name")
    val repoDesc = composeTestRule.onNodeWithTag("repo_desc")
    val forksTitle = composeTestRule.onNodeWithTag("forks_title")
    val forksIcon = composeTestRule.onNodeWithTag("forks_icon")
    val issuesTitle = composeTestRule.onNodeWithTag("issues_title")
    val issuesIcon = composeTestRule.onNodeWithTag("issues_icon")
    val issuesCount = composeTestRule.onNodeWithTag("issues_count")
    val programmingLanguage = composeTestRule.onNodeWithTag("programming_language")
    val readme = composeTestRule.onNodeWithTag("repo_readme")
    val repFiles = composeTestRule.onNodeWithTag("repo_files")
    val addButton = composeTestRule.onNodeWithTag("add_button")
    val dropDownMenu = composeTestRule.onNodeWithTag("dropDownMenu")
    val createPullRequest = composeTestRule.onNodeWithTag("create_pr_item")
    val createIssues = composeTestRule.onNodeWithTag("create_issues_item")
    val addFavouritesButton = composeTestRule.onNodeWithTag("add_fav_button")

    fun checkVisibleNow(
        repoName: String,
        repoDesc: String,
        forksCount: String,
        issuesCount: String,
        programmingLanguage: String,
        readme: String,
    ) {
        this.repoName
            .assertTextEquals(repoName)
            .assertIsDisplayed()

        this.repoDesc
            .assertTextEquals(repoDesc)
            .assertIsDisplayed()

        this.forksTitle
            .assertTextEquals(" $forksCount forks")
            .assertIsDisplayed()

        this.forksIcon
            .assertIsDisplayed()

        this.issuesCount
            .assertTextEquals(issuesCount)

        this.issuesTitle
            .assertTextEquals("Issues")
            .assertIsDisplayed()

        this.issuesIcon
            .assertIsDisplayed()

        this.programmingLanguage
            .assertTextEquals(programmingLanguage)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(
            text = readme,
            substring = true,
            ignoreCase = true,
            useUnmergedTree = true
        ).assertExists()
        this.addFavouritesButton
            .assertIsDisplayed()
            .assertHasClickAction()

        repFiles
            .assertTextEquals("Code")

        addButton
            .assertHasClickAction()
            .assertIsDisplayed()

        dropDownMenu
            .assertDoesNotExist()

        createIssues
            .assertDoesNotExist()

        createPullRequest
            .assertDoesNotExist()
    }

    fun clickAddButton() {
        addButton
            .performClick()

    }

    fun checkDropDownItemsIsVisible() {

        createIssues
            .assertTextEquals("New Issue")
            .assertIsDisplayed()

        createPullRequest
            .assertTextEquals("New Pull Request")
            .assertIsDisplayed()

    }

    fun clickCreateIssues() {
        createIssues
            .performClick()

    }
}

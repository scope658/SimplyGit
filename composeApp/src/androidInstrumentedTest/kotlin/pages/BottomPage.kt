package pages

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class BottomPage(private val composeTestRule: ComposeTestRule) {

    fun clickSearch() {
        composeTestRule.onNodeWithTag("bottom_search")
            .performClick()

    }

    fun clickFavourites() {
        composeTestRule.onNodeWithTag("bottom_favourites")
            .performClick()
    }

    fun clickProfile() {
        composeTestRule.onNodeWithTag("bottom_profile")
            .performClick()
    }

}

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.example.project.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(value = AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        AppAssets.imageUrl = "fakeImageUrl"
        composeTestRule.activityRule.scenario.recreate()
    }

    @Test
    fun navigateToLoginScreen() {
        val onboardingPage = OnboardingPage(composeTestRule)

        onboardingPage.checkVisibleNow()
        onboardingPage.clickContinueButton()
        onboardingPage.checkNotVisibleNow()

        val loginPage = LoginPage(composeTestRule)

        loginPage.checkVisibleNow()
        loginPage.typeLogin(login = "userName")
        loginPage.typePassword(password = "qwerty123")

        composeTestRule.activityRule.scenario.recreate()
        loginPage.checkInputValues(loginField = "userName", passwordField = "qwerty123")

        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.activityRule.scenario.onActivity {
            assertTrue(it.isFinishing)
        }
    }
}
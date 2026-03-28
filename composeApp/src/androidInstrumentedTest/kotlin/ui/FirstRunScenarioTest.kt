package ui

import OnboardingPage
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import org.example.project.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pages.LoginPage
import pages.MainPage


@RunWith(AndroidJUnit4::class)
class FirstRunScenarioTest : AbstractTest() {


    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Before
    fun setUp() {
        abstractSetUp()
    }

    @Test
    fun userFirstRunTest() {
        fakeDataStoreManager.mockOnboardedFlag(false)
        fakeDataStoreManager.mockToken(null)

        ActivityScenario.launch(MainActivity::class.java).use {

            val onboardingPage = OnboardingPage(composeTestRule)
            onboardingPage.checkVisibleNow(
                imageRes = Res.drawable.first_onboarding_image,
                onboardingTitle = "Search repositories",
                onboardingText = "Find public GitHub repositories using search."
            )
            onboardingPage.clickSkipButton()

            val loginPage = LoginPage(composeTestRule)
            loginPage.checkVisibleNow()
        }
    }

    @Test
    fun userAlreadyOnboarded() {
        fakeDataStoreManager.mockOnboardedFlag(true)
        fakeDataStoreManager.mockToken(null)

        ActivityScenario.launch(MainActivity::class.java).use {
            val loginPage = LoginPage(composeTestRule)
            loginPage.checkVisibleNow()

            loginPage.clickSignInButton()

            val mainPage = MainPage(composeTestRule)
            mainPage.checkVisibleNow()

        }
    }

    @Test
    fun userAlreadyOnboardedAndLoggedIn() {
        fakeDataStoreManager.mockOnboardedFlag(true)
        fakeDataStoreManager.mockToken("fakeToken")

        ActivityScenario.launch(MainActivity::class.java).use {
            val mainPage = MainPage(composeTestRule)
            mainPage.checkVisibleNow()
        }
    }
}

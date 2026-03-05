import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.MainActivity
import org.example.project.MockData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pages.LoginPage
import pages.MainPage


@RunWith(value = AndroidJUnit4::class)
class ScenarioTest : AbstractTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullOnboardingScreen() {
        val onboardingPage = OnboardingPage(composeTestRule)

        onboardingPage.checkVisibleNow(
            imageRes = Res.drawable.first_onboarding_image,
            onboardingTitle = "Search repositories",
            onboardingText = "Find public GitHub repositories using search."
        )
        onboardingPage.clickContinueButton()

        onboardingPage.checkVisibleNow(
            imageRes = Res.drawable.second_onboarding_image,
            "Add to favorites",
            "Save repositories for quick access later."
        )

        onboardingPage.clickContinueButton()

        composeTestRule.activityRule.assertAfterAndBeforeRecreate(
            block = {
                onboardingPage.checkVisibleNow(
                    imageRes = Res.drawable.third_onboarding_image,
                    "Simple navigation",
                    "Repositories, favorites, and profile are available in the bottom menu."
                )
            }
        )

        onboardingPage.clickContinueButton()
        onboardingPage.checkNotVisibleNow()

        val loginPage = LoginPage(composeTestRule)
        loginPage.checkVisibleNow()
    }

    @Test
    fun skipOnboardingScreen() {
        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.checkVisibleNow(
            imageRes = Res.drawable.first_onboarding_image,
            onboardingTitle = "Search repositories",
            onboardingText = "Find public GitHub repositories using search."
        )

        onboardingPage.clickSkipButton()
        onboardingPage.checkNotVisibleNow()

        val loginPage = LoginPage(composeTestRule)
        loginPage.checkVisibleNow()
    }

    @Test
    fun successLoginIn() {
        //TODO ADD MOCK SUCCESS IN LOGIN REPO
        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)
        loginPage.checkVisibleNow()

        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()
        mainPage.checkUserRepositories(userRepositories = MockData.mockedUserRepositoriesUi)
    }

    @Test
    fun failureLoginIn() {
        //TODO ADD MOCK FAILURE IN LOGIN REPO
        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)

        loginPage.clickSignInButton()
        loginPage.checkErrorMessageIsVisible("User cancelled")
    }
}


abstract class AbstractTest {
    protected fun ActivityScenarioRule<*>.assertAfterAndBeforeRecreate(
        block: () -> Unit,
    ) {
        block()
        this.scenario.recreate()
        block()
    }
}



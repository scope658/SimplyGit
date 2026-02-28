import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
    fun emptyFieldsThenInvalidInputs() {
        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)

        loginPage.checkVisibleNow()
        loginPage.typeLogin(" ")
        loginPage.typePassword(" ")

        loginPage.checkInputValues(loginField = "", passwordField = "")

        loginPage.checkButtonIsNotActive()

        loginPage.typePassword("1")
        loginPage.checkInputValues(loginField = "", passwordField = "1")

        loginPage.checkButtonIsNotActive()

        loginPage.typeLogin("a")
        loginPage.checkInputValues("a", "1")

        loginPage.checkButtonIsActive()
        loginPage.clickSignInButton()

        loginPage.checkInputsIsNotValid()
    }

    @Test
    fun validLoginInputs() {
        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)

        loginPage.typeLogin("admin")
        loginPage.typePassword("admin1234")

        composeTestRule.activityRule.assertAfterAndBeforeRecreate(
            block = {
                loginPage.checkInputValues(
                    loginField = "admin",
                    passwordField = "admin1234"
                )
            }
        )
        loginPage.checkButtonIsActive()

        loginPage.clickSignInButton()
        //TODO add ListPage check visible
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
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.AuthWrapper
import org.example.project.FakeAuthWrapper
import org.example.project.MainActivity
import org.example.project.MockData
import org.example.project.login.di.loginModule
import org.example.project.main.data.cloud.FakeGithubApi
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.di.mainModule
import org.example.project.onboarding.di.onboardingModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import pages.LoginPage
import pages.MainPage


@RunWith(value = AndroidJUnit4::class)
class ScenarioTest : AbstractTest(), KoinTest {

    private lateinit var authWrapper: FakeAuthWrapper
    private lateinit var githubApi: FakeGithubApi

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        abstractSetUp()
    }

    @After
    fun tearDown() {
        authWrapper.setException(null)
        githubApi.setException(null)

    }
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

        composeTestRule.activityRule.assertBeforeAndAfterRecreate(
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
    fun failureLoginInThenSuccess() {
        authWrapper.setException(IllegalStateException("User cancelled"))

        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)

        loginPage.clickSignInButton()
        loginPage.checkErrorMessageIsVisible("User cancelled")

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            loginPage.checkErrorMessageIsVisible("User cancelled")
        }
        authWrapper.setException(null)
        loginPage.clickSignInButton()

        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()
        mainPage.checkUserRepositories(userRepositories = MockData.mockedUserRepositoriesUi)
    }

    @Test
    fun failureThenRetryFirstRunAfterLogin() {
        githubApi.setException(IllegalStateException("something went wrong"))
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.checkFailureState(errorMessage = "something went wrong")
        githubApi.setException(null)

        mainPage.clickRetryButton()
        mainPage.checkUserRepositories(MockData.mockedUserRepositoriesUi)
    }

    @Test
    fun successSearchQueryResult() {
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)

        mainPage.inputQuery("search")
        mainPage.checkQueryText("search")
        mainPage.checkUserRepositories(userRepositories = MockData.mockedSearchRepositoriesUi)
    }

    @Test
    fun emptySearchQueryResult() {
        githubApi.setEmptySearchResult()
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.checkUserRepositories(
            MockData.mockedUserRepositoriesUi
        )
        mainPage.inputQuery(query = "qweqwqweewqewqqweqwe")
        mainPage.checkQueryText("qweqwqweewqewqqweqwe")
        mainPage.checkEmptyResultStateVisible()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            mainPage.checkEmptyResultStateVisible()
        }
    }

    @Test
    fun emptyUserRepositories() {
        githubApi.setEmptyUserRepoResult()
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.checkEmptyResultStateVisible()
    }

    @Test
    fun failureThenSuccessSearchQueryResult() {
        githubApi.setException(IllegalStateException("something went wrong"))
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.inputQuery("search github repository")
        mainPage.checkQueryText("search github repository")
        mainPage.checkFailureState(errorMessage = "something went wrong")

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            mainPage.checkFailureState(errorMessage = "something went wrong")
        }

        githubApi.setException(null)
        mainPage.clearInputText()
        mainPage.inputQuery("new input query")
        mainPage.checkQueryText("new input query")

        mainPage.checkUserRepositories(userRepositories = MockData.mockedSearchRepositoriesUi)

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            mainPage.checkUserRepositories(userRepositories = MockData.mockedSearchRepositoriesUi)
        }
    }

    @Test
    fun retryFailureSearchResult() {
        githubApi.setException(IllegalStateException("something went wrong"))
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.inputQuery("input")
        mainPage.checkQueryText("input")

        mainPage.checkFailureState(errorMessage = "something went wrong")
        githubApi.setException(null)
        mainPage.clickRetryButton()

        mainPage.checkUserRepositories(userRepositories = MockData.mockedSearchRepositoriesUi)

    }

    @Test
    fun userAlreadyOnboarded() {
        //TODO ADD FAKE ONBOARDED TRUE TO DATA STORE MANAGER
        val loginPage = LoginPage(composeTestRule)
        loginPage.checkVisibleNow()

        loginPage.clickSignInButton()

        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()

    }

    @Test
    fun userAlreadyOnboardedAndLoggedIn() {
        //TODO ADD FAKE ONBOARDED TRUE TO DATA STORE MANAGER
        //TODO ADD FAKE LOGGED IN TRUE TO DATA STORE MANAGER
        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()

    }
}


abstract class AbstractTest {

    protected fun ActivityScenarioRule<*>.assertBeforeAndAfterRecreate(
        block: () -> Unit,
    ) {
        block()
        this.scenario.recreate()
        block()
    }

    protected fun skipOnboardingAndLogin(composeTestRule: ComposeTestRule) {
        val onboardingPage = OnboardingPage(composeTestRule = composeTestRule)
        onboardingPage.clickSkipButton()
        val loginPage = LoginPage(composeTestRule)
        loginPage.clickSignInButton()
    }
}




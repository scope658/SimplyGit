import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.MainActivity
import org.example.project.MockData
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pages.LoginPage
import pages.MainPage
import pages.ProfilePage


@RunWith(value = AndroidJUnit4::class)
class ScenarioTest : AbstractTest() {


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
        loginPage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            loginPage.checkLoginErrorMessage("User cancelled")
        }
        authWrapper.setException(null)
        loginPage.clickSignInButton()

        loginPage.waitUntilLoadingDoesNotExist()
        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()
        mainPage.waitUntilLoadingDoesNotExist()

        mainPage.checkUserRepositories(userRepositories = MockData.mockedUserRepositoriesUi)
    }

    @Test
    fun failureThenRetryFirstRunAfterLogin() {
        githubApi.setException(IllegalStateException("something went wrong"))
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)

        mainPage.waitUntilLoadingDoesNotExist()
        mainPage.checkFailureState(errorMessage = "something went wrong")
        githubApi.setException(null)

        mainPage.clickRetryButton()

        mainPage.waitUntilLoadingDoesNotExist()
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

        mainPage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            mainPage.checkFailureState(errorMessage = "something went wrong")
        }

        githubApi.setException(null)
        mainPage.clearInputText()
        mainPage.inputQuery("new input query")
        mainPage.checkQueryText("new input query")

        mainPage.waitUntilLoadingDoesNotExist()

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

        mainPage.waitUntilLoadingDoesNotExist()

        mainPage.checkFailureState(errorMessage = "something went wrong")
        githubApi.setException(null)
        mainPage.clickRetryButton()

        mainPage.waitUntilLoadingDoesNotExist()
        mainPage.checkUserRepositories(userRepositories = MockData.mockedSearchRepositoriesUi)

    }

    @Test
    fun failureThenSuccessLoadUserProfile() {
        skipOnboardingAndLogin(composeTestRule)
        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()

        mainPage.clickProfileIcon()
        mainPage.waitUntilLoadingDoesNotExist()

        val profilePage = ProfilePage(composeTestRule)
        //TODO ADD FAKE FAILURE PROFILE DATA

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            profilePage.checkErrorMessageIsVisible("something went wrong")
        }

        //TODO ADD SUCCESS PROFILE DATA
        profilePage.clickRetryButton()

        profilePage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            profilePage.checkVisibleNow(
                userName = "scope",
                bio = "fake bio",
                repoCount = "12",
                subscribersCount = "23"
            )
        }

        profilePage.clickLogoutButton()

        val loginPage = LoginPage(composeTestRule)
        loginPage.checkVisibleNow()
    }
}


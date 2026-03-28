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
import ui.AbstractTest
import java.net.SocketTimeoutException


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
        authWrapper.setException(false)
        githubApi.isMainPageFailure(false)

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
        authWrapper.setException(true)

        val onboardingPage = OnboardingPage(composeTestRule)
        onboardingPage.clickSkipButton()

        val loginPage = LoginPage(composeTestRule)

        loginPage.clickSignInButton()
        loginPage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            loginPage.checkLoginErrorMessage("Service is temporarily unavailable")
        }
        authWrapper.setException(false)
        loginPage.clickSignInButton()

        loginPage.waitUntilLoadingDoesNotExist()
        val mainPage = MainPage(composeTestRule)
        mainPage.checkVisibleNow()
        mainPage.waitUntilLoadingDoesNotExist()

        mainPage.checkUserRepositories(userRepositories = MockData.mockedUserRepositoriesUi)
    }

    @Test
    fun failureThenRetryFirstRunAfterLogin() {
        githubApi.isMainPageFailure(true, SocketTimeoutException())

        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)

        mainPage.waitUntilLoadingDoesNotExist()
        mainPage.checkFailureState(errorMessage = "No internet connection")
        githubApi.isMainPageFailure(false)

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
        githubApi.isMainPageFailure(true)
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.inputQuery("search github repository")
        mainPage.checkQueryText("search github repository")

        mainPage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            mainPage.checkFailureState(errorMessage = "Service is temporarily unavailable")
        }

        githubApi.isMainPageFailure(false)
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
        githubApi.isMainPageFailure(true)
        skipOnboardingAndLogin(composeTestRule)

        val mainPage = MainPage(composeTestRule)
        mainPage.inputQuery("input")
        mainPage.checkQueryText("input")

        mainPage.waitUntilLoadingDoesNotExist()

        mainPage.checkFailureState(errorMessage = "Service is temporarily unavailable")
        githubApi.isMainPageFailure(false)
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

        githubApi.isUserProfileIsFailure(true)
        val profilePage = ProfilePage(composeTestRule)


        profilePage.waitUntilLoadingDoesNotExist()

        composeTestRule.activityRule.assertBeforeAndAfterRecreate {
            profilePage.checkErrorMessageIsVisible("Service is temporarily unavailable")
        }

        githubApi.isUserProfileIsFailure(false)
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


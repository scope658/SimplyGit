import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.example.project.AuthWrapper
import org.example.project.FakeAuthWrapper
import org.example.project.app.di.appModule
import org.example.project.core.cache.DataStoreManager
import org.example.project.login.di.loginModule
import org.example.project.main.data.cloud.FakeGithubApi
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.di.mainModule
import org.example.project.onboarding.di.onboardingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import pages.LoginPage

abstract class AbstractTest : KoinTest {

    protected lateinit var authWrapper: FakeAuthWrapper
    protected lateinit var githubApi: FakeGithubApi
    protected lateinit var fakeDataStoreManager: FakeDataStoreManager

    protected fun abstractSetUp() {

        fakeDataStoreManager = FakeDataStoreManager()
        authWrapper = FakeAuthWrapper()
        githubApi = FakeGithubApi()

        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                onboardingModule,
                loginModule,
                mainModule,
                appModule,
                module {
                    single<AuthWrapper> { authWrapper }
                    single<GithubApi> { githubApi }
                    single<DataStoreManager.Read> { fakeDataStoreManager }
                }
            )
        }
    }

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




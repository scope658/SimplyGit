package ui


import OnboardingPage
import android.content.Context
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.example.project.AuthWrapper
import org.example.project.FakeAuthWrapper
import org.example.project.app.data.FakeGeneralDataStoreManager
import org.example.project.app.di.appModule
import org.example.project.core.cache.DataStoreManager
import org.example.project.core.cache.cacheModule
import org.example.project.core.cache.db.AppDatabase
import org.example.project.login.di.loginModule
import org.example.project.main.data.cloud.FakeGithubApi
import org.example.project.main.data.cloud.GithubApi
import org.example.project.main.di.mainModule
import org.example.project.onboarding.di.onboardingModule
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import pages.LoginPage

abstract class AbstractTest : KoinTest {

    protected lateinit var authWrapper: FakeAuthWrapper
    protected lateinit var githubApi: FakeGithubApi
    protected lateinit var fakeDataStoreManager: FakeGeneralDataStoreManager

    protected fun abstractSetUp() {

        fakeDataStoreManager = FakeGeneralDataStoreManager()
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
                cacheModule,
                profileModule,
                module {
                    single<AuthWrapper> { authWrapper }
                    single<GithubApi> { githubApi }
                    single<DataStoreManager.Read> { fakeDataStoreManager }
                    single<DataStoreManager.FinishOnboarding> { fakeDataStoreManager }
                    single<DataStoreManager.ReadToken> { fakeDataStoreManager }
                    single<DataStoreManager.SaveToken> { fakeDataStoreManager }
                    single<DataStoreManager.TokenOperations> { fakeDataStoreManager }
                    single<ProfileGithubApi> { githubApi }
                    single<RoomDatabase.Builder<AppDatabase>> {
                        val context = ApplicationProvider.getApplicationContext<Context>()
                        Room.inMemoryDatabaseBuilder<AppDatabase>(
                            context,
                            AppDatabase::class.java
                        )
                    }
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




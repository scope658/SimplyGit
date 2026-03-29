package cache

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.project.core.data.cache.DataStoreManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse


@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {

    private lateinit var dataStoreManager: DataStoreManager.All

    @Before
    fun setUp() {
        dataStoreManager =
            DataStoreManager.Base(TestDataStore.testDataStore)
    }

    @Test
    fun userTokenScenario() = runBlocking {
        val emptyToken: String = dataStoreManager.userToken()
        assertEquals("", emptyToken)

        dataStoreManager.saveUserToken(token = FAKE_TOKEN)

        val actualToken: String? = dataStoreManager.userToken()
        assertEquals(expected = FAKE_TOKEN, actual = actualToken)
    }


    @Test
    fun onboardedScenario() = runBlocking {
        val emptyOnboardedFlag: Boolean = dataStoreManager.isOnboarded()
        assertFalse(emptyOnboardedFlag)

        dataStoreManager.finishOnboarding()

        val actualOnboardedFlag: Boolean = dataStoreManager.isOnboarded()

        assertEquals(true, actualOnboardedFlag)
    }

    companion object {
        private const val FAKE_TOKEN = "fakeToken"
    }
}

object TestDataStore {
    private const val TEST_FILE_NAME = "test_file"
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    val testScope = CoroutineScope(Dispatchers.Unconfined)
    val testDataStore = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { testContext.preferencesDataStoreFile(TEST_FILE_NAME) })
}

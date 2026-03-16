package cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import org.example.project.core.cache.db.AppDatabase
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(value = AndroidJUnit4::class)
class RoomTest {
    private lateinit var db: AppDatabase
    private lateinit var profileDao: ProfileDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
        ).build()
        profileDao = db.profileDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun emptyThenSaveUserProfile() = runBlocking {
        var actualUserProfile: ProfileCache? = profileDao.readUserProfile()
        assertNull(actualUserProfile)

        profileDao.saveUserProfile(profileCache = expectedProfileCache)

        actualUserProfile = profileDao.readUserProfile()
        assertEquals(expectedProfileCache, actualUserProfile)
    }

    @Test
    fun clearUserProfile() = runBlocking {
        profileDao.saveUserProfile(profileCache = expectedProfileCache)

        val userProfile = profileDao.readUserProfile()
        assertEquals(expectedProfileCache, userProfile)

        profileDao.clearAll()

        val actualUserProfile = profileDao.readUserProfile()
        assertNull(actualUserProfile)
    }

    @Test
    fun addProfileUserWithSameId() = runBlocking {
        profileDao.saveUserProfile(profileCache = expectedProfileCache)

        val userProfile = profileDao.readUserProfile()
        assertEquals(expectedProfileCache, userProfile)

        profileDao.saveUserProfile(profileCache = expectedProfileCache.copy(userName = "new user name"))

        val changedUserProfile = profileDao.readUserProfile()
        assertEquals(
            expected = expectedProfileCache.copy(userName = "new user name"),
            changedUserProfile
        )
    }
}

private val expectedProfileCache = ProfileCache(
    userId = 1L,
    avatar = "fakeAvatar",
    userName = "fake name",
    bio = "fake bio",
    repoCount = 12,
    subscribersCount = 23,
)




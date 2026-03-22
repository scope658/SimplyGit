package org.example.project.app.data


import kotlinx.coroutines.runBlocking
import org.example.project.core.data.cache.DataStoreManager
import kotlin.test.Test
import kotlin.test.assertEquals

class AppRepositoryTest {


    @Test
    fun `user token and onboarding state`() = runBlocking {

        val dataStoreManager = FakeDataStoreManager()
        dataStoreManager.mockToken(null)
        dataStoreManager.mockOnboardedFlag(true)

        val appRepository = AppRepositoryImpl(
            dataStoreManager = dataStoreManager,
        )
        var isTokenSaved: Boolean = appRepository.isTokenSaved()
        assertEquals(actual = isTokenSaved, expected = false)

        val isUserOnboarded: Boolean = appRepository.isUserOnboarded()
        assertEquals(actual = isUserOnboarded, expected = true)

        dataStoreManager.mockToken(token = "fakeToken")

        isTokenSaved = appRepository.isTokenSaved()
        assertEquals(actual = isTokenSaved, expected = true)
    }
}
private class FakeDataStoreManager :
    DataStoreManager.Read {

    private var token: String? = null
    private var onboardingFlag = false

    fun mockToken(token: String?) {
        this.token = token
    }

    fun mockOnboardedFlag(flag: Boolean) {
        this.onboardingFlag = flag
    }

    override suspend fun userToken(): String {
        return token ?: ""
    }

    override suspend fun isOnboarded(): Boolean {
        return onboardingFlag
    }

}

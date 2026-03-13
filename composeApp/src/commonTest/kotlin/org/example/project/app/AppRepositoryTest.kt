package org.example.project.app

import kotlinx.coroutines.runBlocking
import org.example.project.app.data.AppRepositoryImpl
import org.example.project.core.cache.DataStoreManager
import kotlin.test.Test
import kotlin.test.assertEquals


class AppRepositoryTest {


    @Test
    fun `user token and onboarding state`() = runBlocking {
        val dataStoreManager = FakeDataStoreManager(
            token = null,
            isOnboarded = true,
        )
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

private class FakeDataStoreManager(private var token: String?, private val isOnboarded: Boolean) :
    DataStoreManager.Read {

    fun mockToken(token: String?) {
        this.token = token
    }

    override suspend fun userToken(): String? {
        return token
    }

    override suspend fun isOnboarded(): Boolean {
        return isOnboarded
    }

}

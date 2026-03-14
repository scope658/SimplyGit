package org.example.project.app.data

import FakeDataStoreManager
import kotlinx.coroutines.runBlocking
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

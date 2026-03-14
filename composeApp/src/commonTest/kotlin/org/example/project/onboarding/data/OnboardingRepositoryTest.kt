package org.example.project.onboarding.data

import kotlinx.coroutines.runBlocking
import org.example.project.core.cache.DataStoreManager
import kotlin.test.Test
import kotlin.test.assertEquals

class OnboardingRepositoryTest {
    @Test
    fun `save finish onboarding flag`() = runBlocking {
        val fakeDataStoreManager =
            FakeOnboardingDataStoreManager()
        val onboardingRepository = OnboardingRepositoryImpl(dataStoreManager = fakeDataStoreManager)

        onboardingRepository.onboardingFinished()
        fakeDataStoreManager.checkFinishCalled(1)
    }
}

private class FakeOnboardingDataStoreManager : DataStoreManager.FinishOnboarding {
    private var actualCalledTimes = 0
    override suspend fun finishOnboarding() {
        actualCalledTimes++
    }

    fun checkFinishCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, actualCalledTimes)
    }
}

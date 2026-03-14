package org.example.project.onboarding.data

import org.example.project.core.cache.DataStoreManager
import org.example.project.onboarding.domain.OnboardingRepository

class OnboardingRepositoryImpl(private val dataStoreManager: DataStoreManager.FinishOnboarding) :
    OnboardingRepository {

    override suspend fun onboardingFinished() {
        dataStoreManager.finishOnboarding()
    }
}

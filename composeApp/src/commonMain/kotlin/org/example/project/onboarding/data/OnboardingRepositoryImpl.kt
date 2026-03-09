package org.example.project.onboarding.data

import org.example.project.onboarding.domain.OnboardingRepository

class OnboardingRepositoryImpl : OnboardingRepository {

    override suspend fun onboardingFinished() {
        //TODO add flag to data store
    }
}
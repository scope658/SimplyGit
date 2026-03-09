package org.example.project.onboarding.domain

interface OnboardingRepository {
    suspend fun onboardingFinished()
}
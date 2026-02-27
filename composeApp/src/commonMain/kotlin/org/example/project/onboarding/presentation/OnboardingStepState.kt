package org.example.project.onboarding.presentation


interface OnboardingStepState {
    val nextPage: OnboardingStepState?
    fun currentState(): OnboardingPage
}
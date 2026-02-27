package org.example.project.onboarding.presentation

sealed class OnboardingEvent {
    object Finished : OnboardingEvent()
}
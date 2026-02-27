package org.example.project.onboarding.di


import org.example.project.core.RunAsync
import org.example.project.onboarding.data.OnboardingRepositoryImpl
import org.example.project.onboarding.domain.OnboardingRepository
import org.example.project.onboarding.presentation.OnboardingStepState
import org.example.project.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.dsl.viewModel

import org.koin.dsl.module


val onboardingModule = module {
    single<RunAsync> { RunAsync.Base() }
    single<OnboardingRepository> { OnboardingRepositoryImpl() }
    single<OnboardingStepState> { OnboardingStepState.FirstPage }
    viewModel {
        OnboardingViewModel(
            savedStateHandle = get(),
            onboardingRepository = get(),
            runAsync = get(),
            onboardingStepState = get(),
        )
    }
}
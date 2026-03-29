package org.example.project.onboarding.di


import org.example.project.core.data.cache.DataStoreManager
import org.example.project.core.presentation.RunAsync
import org.example.project.onboarding.data.OnboardingRepositoryImpl
import org.example.project.onboarding.domain.OnboardingRepository
import org.example.project.onboarding.presentation.OnboardingStepState
import org.example.project.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val onboardingModule = module {
    factory<RunAsync> { RunAsync.Base() }
    factory<DataStoreManager.FinishOnboarding> { DataStoreManager.Base(dataStore = get()) }
    factory<OnboardingRepository> { OnboardingRepositoryImpl(dataStoreManager = get()) }
    factory<OnboardingStepState> { OnboardingStepState.FirstPage }
    viewModel {
        OnboardingViewModel(
            savedStateHandle = get(),
            onboardingRepository = get(),
            runAsync = get(),
        )
    }
}

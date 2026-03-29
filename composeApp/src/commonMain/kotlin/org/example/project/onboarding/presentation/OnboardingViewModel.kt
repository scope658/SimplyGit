package org.example.project.onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.presentation.RunAsync
import org.example.project.onboarding.domain.OnboardingRepository
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class OnboardingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingRepository: OnboardingRepository,
    private val runAsync: RunAsync,
) : ViewModel(), OnboardingActions {

    private var savedState: OnboardingStepState by savedStateHandle.saved(
        key = ONBOARDING_STEP_KEY,
        init = {
            OnboardingStepState.FirstPage
        }
    )

    private val _onboardingScreenState = MutableStateFlow(
        value = OnboardingScreenState(
            onboardingStepState = savedState,
            onboardingUiState = savedState.currentState(),
        )
    )
    val onboardingScreenState = _onboardingScreenState.asStateFlow()


    private val _onboardingEvent: MutableSharedFlow<OnboardingEvent> = MutableSharedFlow()
    val onboardingEvent = _onboardingEvent.asSharedFlow()

    override fun nextPage() {
        val next = savedState.nextPage
        if (next != null) {
            savedState = next
            _onboardingScreenState.update { onboardingScreenState ->
                onboardingScreenState.copy(
                    onboardingStepState = savedState,
                    onboardingUiState = savedState.currentState(),
                )
            }
        } else {
            skipOnboarding()
        }
    }

    override fun skipOnboarding() {

        runAsync.runAsync(
            viewModelScope,
            background = { },
            ui = { _onboardingEvent.emit(OnboardingEvent.Finished) }
        )

    }

    override fun finishOnboarding() {
        runAsync.runAsync(
            scope = viewModelScope,
            { onboardingRepository.onboardingFinished() },
            { }
        )
    }

    companion object {
        private const val ONBOARDING_STEP_KEY = "ONBOARDING_STEP_KEY"
    }
}


data class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
    val image: DrawableResource,
)

package org.example.project.onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.example.project.core.RunAsync
import org.example.project.onboarding.domain.OnboardingRepository
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class OnboardingViewModel(
    savedStateHandle: SavedStateHandle,
    private val onboardingRepository: OnboardingRepository,
    private val runAsync: RunAsync,
    onboardingStepState: OnboardingStepState,
) : ViewModel(), OnboardingActions {

    private val _onboardingStepStateFlow: MutableStateFlow<OnboardingStepState> =
        savedStateHandle.getMutableStateFlow(ONBOARDING_STEP_KEY, onboardingStepState)
    val onboardingStepStateFlow = _onboardingStepStateFlow.asStateFlow()

    private val _onboardingPageFlow: MutableStateFlow<OnboardingPage> =
        MutableStateFlow(value = onboardingStepState.currentState()) //TODO ADD ONE ONBOARDING UI STATE FLOW
    val onboardingPageFlow = _onboardingPageFlow.asStateFlow()

    private val _onboardingEvent: MutableSharedFlow<OnboardingEvent> = MutableSharedFlow()
    val onboardingEvent = _onboardingEvent.asSharedFlow()

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = _onboardingStepStateFlow
                .map { it.currentState() },
            onEach = { onboardingPage ->
                _onboardingPageFlow.value = onboardingPage
            },
        )
    }

    override fun nextPage() {
        val next = onboardingStepStateFlow.value.nextPage
        if (next != null) {
            _onboardingStepStateFlow.value = next
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

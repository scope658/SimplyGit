package org.example.project.onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_third_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_third_title
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.core.FakeRunAsync
import org.example.project.onboarding.domain.OnboardingRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnboardingViewModelTest {

    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var fakeOnboardingRepository: FakeOnboardingRepository
    private lateinit var fakeOnboardingRunAsync: FakeRunAsync
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeTest
    fun setUp() {
        fakeOnboardingRepository = FakeOnboardingRepository()
        fakeOnboardingRunAsync = FakeRunAsync()
        savedStateHandle = SavedStateHandle()
        onboardingViewModel = OnboardingViewModel(
            savedStateHandle = savedStateHandle,
            onboardingRepository = fakeOnboardingRepository,
            runAsync = fakeOnboardingRunAsync,
        )
    }

    @Test
    fun `navigating to next pages`() {
        val onboardingScreenState: StateFlow<OnboardingScreenState> =
            onboardingViewModel.onboardingScreenState

        assertEquals(OnboardingStepState.FirstPage, onboardingScreenState.value.onboardingStepState)
        assertEquals(expectedFirstPage, onboardingScreenState.value.onboardingUiState)

        onboardingViewModel.nextPage()
        assertEquals(
            OnboardingStepState.SecondPage,
            onboardingScreenState.value.onboardingStepState
        )
        assertEquals(expectedSecondPage, onboardingScreenState.value.onboardingUiState)

        onboardingViewModel.nextPage()
        assertEquals(OnboardingStepState.ThirdPage, onboardingScreenState.value.onboardingStepState)
        assertEquals(expectedThirdPage, onboardingScreenState.value.onboardingUiState)
    }

    @Test
    fun `finishing onboarding after third page`() = runBlocking {
        val emitedValues = mutableListOf<OnboardingEvent>()
        val onboardingScreenState: StateFlow<OnboardingScreenState> =
            onboardingViewModel.onboardingScreenState

        assertEquals(OnboardingStepState.FirstPage, onboardingScreenState.value.onboardingStepState)
        assertEquals(expectedFirstPage, onboardingScreenState.value.onboardingUiState)

        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val job = launch(Dispatchers.Unconfined) {
            onboardingEventSharedFlow
                .collect {
                    emitedValues.add(it)
                }
        }
        onboardingViewModel.nextPage()
        onboardingViewModel.nextPage()
        onboardingViewModel.nextPage()

        assertEquals(1, emitedValues.size)
        emitedValues.forEach { onboardingEvent ->
            assertEquals(OnboardingEvent.Finished, onboardingEvent)
        }

        job.cancel()
    }

    @Test
    fun `assert no event`() = runBlocking {
        val emitedEvents = mutableListOf<OnboardingEvent>()
        val onboardingScreenState: StateFlow<OnboardingScreenState> =
            onboardingViewModel.onboardingScreenState

        assertEquals(OnboardingStepState.FirstPage, onboardingScreenState.value.onboardingStepState)
        assertEquals(expectedFirstPage, onboardingScreenState.value.onboardingUiState)


        onboardingViewModel.nextPage()

        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val job = launch(Dispatchers.Unconfined) {
            onboardingEventSharedFlow
                .collect {
                    emitedEvents.add(it)
                }
        }
        onboardingViewModel.nextPage()

        job.cancel()

        assertTrue(emitedEvents.isEmpty())
    }

    @Test
    fun `trigger onboarding repository`() {
        onboardingViewModel.finishOnboarding()
        fakeOnboardingRepository.checkOnboardingFinishedCalled(expectedCalledTimes = 1)
    }

    @Test
    fun `trigger skip onboarding`() = runBlocking {
        val onboardingScreenState: StateFlow<OnboardingScreenState> =
            onboardingViewModel.onboardingScreenState

        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val job = launch(Dispatchers.Unconfined) {
            onboardingEventSharedFlow
                .collect {
                    assertEquals(OnboardingEvent.Finished, it)
                }
        }
        onboardingViewModel.skipOnboarding()


        assertEquals(OnboardingStepState.FirstPage, onboardingScreenState.value.onboardingStepState)
        assertEquals(expectedFirstPage, onboardingScreenState.value.onboardingUiState)

        job.cancel()
    }

}


private val expectedFirstPage = OnboardingPage(
    title = Res.string.onboarding_first_title,
    description = Res.string.onboarding_first_desc,
    image = Res.drawable.first_onboarding_image,
)
private val expectedSecondPage = OnboardingPage(
    title = Res.string.onboarding_second_title,
    description = Res.string.onboarding_second_desc,
    image = Res.drawable.second_onboarding_image,
)
private val expectedThirdPage = expectedFirstPage.copy(
    title = Res.string.onboarding_third_title,
    description = Res.string.onboarding_third_desc,
    image = Res.drawable.third_onboarding_image,
)


private class FakeOnboardingRepository : OnboardingRepository {

    private var actualCalledTimes = 0

    override suspend fun onboardingFinished() {
        actualCalledTimes++
    }

    fun checkOnboardingFinishedCalled(expectedCalledTimes: Int) {
        assertEquals(expectedCalledTimes, actualCalledTimes)
    }

}

package org.example.project

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.mock_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_third_title
import org.example.project.core.FakeRunAsync
import org.example.project.onboarding.domain.OnboardingRepository
import org.example.project.onboarding.presentation.OnboardingEvent
import org.example.project.onboarding.presentation.OnboardingPage
import org.example.project.onboarding.presentation.OnboardingStepState
import org.example.project.onboarding.presentation.OnboardingViewModel
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
            onboardingStepState = FakeOnboardingStepState.FakeFirstPage
        )
    }

    @Test
    fun `navigating to next pages`() {
        val stepState: StateFlow<OnboardingStepState> = onboardingViewModel.onboardingStepStateFlow
        val pageState: StateFlow<OnboardingPage> = onboardingViewModel.onboardingPageFlow

        assertEquals(FakeOnboardingStepState.FakeFirstPage, stepState.value)
        assertEquals(expectedFirstPage, pageState.value)

        onboardingViewModel.nextPage()
        assertEquals(FakeOnboardingStepState.FakeSecondPage, stepState.value)
        assertEquals(expectedSecondPage, pageState.value)

        onboardingViewModel.nextPage()
        assertEquals(FakeOnboardingStepState.FakeThirdPage, stepState.value)
        assertEquals(expectedThirdPage, pageState.value)

        //process death
        onboardingViewModel = OnboardingViewModel(
            savedStateHandle = savedStateHandle,
            onboardingRepository = fakeOnboardingRepository,
            runAsync = fakeOnboardingRunAsync,
            onboardingStepState = FakeOnboardingStepState.FakeFirstPage
        )

        val newStepState = onboardingViewModel.onboardingStepStateFlow.value
        val newPageState = onboardingViewModel.onboardingPageFlow.value

        assertEquals(FakeOnboardingStepState.FakeThirdPage, newStepState)
        assertEquals(expectedThirdPage, newPageState)
    }

    @Test
    fun `finishing onboarding after third page`() = runBlocking {
        savedStateHandle["ONBOARDING_STEP_KEY"] = FakeOnboardingStepState.FakeThirdPage

        val stepState: StateFlow<OnboardingStepState> = onboardingViewModel.onboardingStepStateFlow
        val pageState: StateFlow<OnboardingPage> = onboardingViewModel.onboardingPageFlow

        assertEquals(FakeOnboardingStepState.FakeThirdPage, stepState.value)
        assertEquals(expectedThirdPage, pageState.value)

        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val job = launch(Dispatchers.Unconfined) {
            onboardingEventSharedFlow
                .collect {
                    println("value is emited $it")
                    assertEquals(OnboardingEvent.Finished, it)
                }
        }
        onboardingViewModel.nextPage()

        assertEquals(FakeOnboardingStepState.FakeThirdPage, stepState.value)
        assertEquals(expectedThirdPage, pageState.value)
        job.cancel()
    }

    @Test
    fun `assert no event`() = runBlocking {
        val emitedEvents = mutableListOf<OnboardingEvent>()
        assertEquals(
            FakeOnboardingStepState.FakeFirstPage,
            onboardingViewModel.onboardingStepStateFlow.value
        )
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
        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val job = launch(Dispatchers.Unconfined) {
            onboardingEventSharedFlow
                .collect {
                    assertEquals(OnboardingEvent.Finished, it)
                }
        }
        onboardingViewModel.skipOnboarding()

        assertEquals(
            FakeOnboardingStepState.FakeFirstPage,
            onboardingViewModel.onboardingStepStateFlow.value
        )
        assertEquals(expectedFirstPage, onboardingViewModel.onboardingPageFlow.value)
        job.cancel()
    }

}

private interface FakeOnboardingStepState : OnboardingStepState {

    @CommonParcelize
    object FakeFirstPage : FakeOnboardingStepState, CommonParcelable {

        override val nextPage: FakeOnboardingStepState? = FakeSecondPage

        override fun currentState(): OnboardingPage {

            return expectedFirstPage
        }
    }

    @CommonParcelize
    object FakeSecondPage : FakeOnboardingStepState, CommonParcelable {

        override val nextPage: FakeOnboardingStepState? = FakeThirdPage

        override fun currentState(): OnboardingPage {

            return expectedSecondPage
        }
    }

    @CommonParcelize
    object FakeThirdPage : FakeOnboardingStepState, CommonParcelable {

        override val nextPage: FakeOnboardingStepState? = null

        override fun currentState(): OnboardingPage {

            return expectedThirdPage
        }
    }
}

private val expectedFirstPage = OnboardingPage(
    title = Res.string.onboarding_first_title,
    description = Res.string.onboarding_first_desc,
    image = Res.drawable.mock_onboarding_image,
)
private val expectedSecondPage = expectedFirstPage.copy(
    title = Res.string.onboarding_second_title,
)
private val expectedThirdPage = expectedFirstPage.copy(
    title = Res.string.onboarding_third_title,
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

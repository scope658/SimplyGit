package org.example.project

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import ktshwnumbertwo.composeapp.generated.resources.Res
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OnboardingViewModelTest {

    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var fakeOnboardingRepository: FakeOnboardingRepository
    private lateinit var fakeOnboardingRunAsync: FakeOnboardingRunAsync
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeTest
    fun setUp() {
        fakeOnboardingRepository = FakeOnboardingRepository()
        fakeOnboardingRunAsync = FakeOnboardingRunAsync()
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
        onboardingViewModel = OnboardingViewModel(
            savedStateHandle = savedStateHandle,
            onboardingRepository = fakeOnboardingRepository,
            runAsync = fakeOnboardingRunAsync,
            onboardingStepState = FakeOnboardingStepState.FakeThirdPage
        )

        val stepState: StateFlow<OnboardingStepState> = onboardingViewModel.onboardingStepStateFlow
        val pageState: StateFlow<OnboardingPage> = onboardingViewModel.onboardingPageFlow

        assertEquals(FakeOnboardingStepState.FakeThirdPage, stepState.value)
        assertEquals(expectedThirdPage, pageState.value)

        onboardingViewModel.nextPage()

        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent

        val actualEmitedValue: OnboardingEvent = withTimeout(1000) {
            onboardingEventSharedFlow.first()
        }
        assertEquals(OnboardingEvent.Finished, actualEmitedValue)
        assertEquals(FakeOnboardingStepState.FakeThirdPage, stepState.value)
        assertEquals(expectedThirdPage, pageState.value)
    }

    @Test
    fun `assert no event`() = runBlocking {
        assertEquals(
            FakeOnboardingStepState.FakeFirstPage,
            onboardingViewModel.onboardingStepStateFlow.value
        )
        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        onboardingViewModel.nextPage()
        val actualEmitedValue = withTimeoutOrNull(100) {
            onboardingEventSharedFlow.first()
        }
        assertNull(actualEmitedValue)
    }

    @Test
    fun `trigger onboarding repository`() {
        onboardingViewModel.finishOnboarding()
        fakeOnboardingRepository.checkOnboardingFinishedCalled(expectedCalledTimes = 1)
    }

    @Test
    fun `trigger skip onboarding`() = runBlocking {
        onboardingViewModel.skipOnboarding()
        val onboardingEventSharedFlow: SharedFlow<OnboardingEvent> =
            onboardingViewModel.onboardingEvent
        val actualEmitedValue: OnboardingEvent = withTimeout(1000) {
            onboardingEventSharedFlow.first()
        }

        assertEquals(OnboardingEvent.Finished, actualEmitedValue)
        assertEquals(
            FakeOnboardingStepState.FakeFirstPage,
            onboardingViewModel.onboardingStepStateFlow.value
        )
        assertEquals(expectedFirstPage, onboardingViewModel.onboardingPageFlow.value)
    }

}

private interface FakeOnboardingStepState : OnboardingStepState {


    object FakeFirstPage : FakeOnboardingStepState {

        override val nextPage: FakeOnboardingStepState? = FakeSecondPage

        override fun currentState(): OnboardingPage {

            return expectedFirstPage
        }
    }

    object FakeSecondPage : FakeOnboardingStepState {

        override val nextPage: FakeOnboardingStepState? = FakeThirdPage

        override fun currentState(): OnboardingPage {

            return expectedSecondPage
        }
    }

    object FakeThirdPage : FakeOnboardingStepState {

        override val nextPage: FakeOnboardingStepState? = null

        override fun currentState(): OnboardingPage {

            return expectedThirdPage
        }
    }
}

private val expectedFirstPage = OnboardingPage(
    title = "1",
    description = "1",
    image = Res.drawable.mock_onboarding_image,
)
private val expectedSecondPage = expectedFirstPage.copy(
    title = "2",
    description = "2",
)
private val expectedThirdPage = expectedFirstPage.copy(
    title = "3",
    description = "3",
)

private class FakeOnboardingRunAsync : RunAsync {

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) {
        runBlocking {
            background.invoke()
        }
    }

    override fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: (T) -> Unit,
    ) {

        scope.launch(Dispatchers.Unconfined) {
            flow.collect {
                onEach.invoke(it)
            }
        }
    }
}

private class FakeOnboardingRepository : OnboardingRepository {

    private var actualCalledTimes = 0

    override suspend fun onboardingFinished() {
        actualCalledTimes++
    }

    fun checkOnboardingFinishedCalled(expectedCalledTimes: Int) {
        assertEquals(expectedCalledTimes, actualCalledTimes)
    }

}
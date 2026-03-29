package viewModels

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_title
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import org.example.project.onboarding.domain.OnboardingRepository
import org.example.project.onboarding.presentation.OnboardingPage
import org.example.project.onboarding.presentation.OnboardingScreenState
import org.example.project.onboarding.presentation.OnboardingStepState
import org.example.project.onboarding.presentation.OnboardingViewModel
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals


@RunWith(AndroidJUnit4::class)
class OnboardingViewModelTest : AbstractViewModelTest() {


    @Test
    fun onboardingProcessDeath() {
        val fakeOnboardingRepository = FakeOnboardingRepository()
        val fakeOnboardingRunAsync = FakeRunAsync()

        viewModelScenario {
            OnboardingViewModel(
                savedStateHandle = createSavedStateHandle(),
                onboardingRepository = fakeOnboardingRepository,
                runAsync = fakeOnboardingRunAsync,
            )
        }.use { scenario ->
            assertEquals(
                OnboardingScreenState(
                    onboardingStepState = OnboardingStepState.FirstPage,
                    onboardingUiState = expectedFirstPage
                ),
                scenario.viewModel.onboardingScreenState.value,
            )

            scenario.viewModel.nextPage()

            scenario.assertBeforeAndAfterProcessDeath {
                assertEquals(
                    OnboardingScreenState(
                        onboardingStepState = OnboardingStepState.SecondPage,
                        onboardingUiState = expectedSecondPage
                    ),
                    scenario.viewModel.onboardingScreenState.value,
                )

            }
        }
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


private class FakeOnboardingRepository : OnboardingRepository {

    override suspend fun onboardingFinished() = Unit
}

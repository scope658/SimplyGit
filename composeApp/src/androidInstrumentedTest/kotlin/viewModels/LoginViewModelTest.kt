package viewModels

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.project.core.domain.FakeManageResource
import org.example.project.login.domain.LoginRepository
import org.example.project.login.presentation.ErrorState
import org.example.project.login.presentation.LoginUiState
import org.example.project.login.presentation.LoginViewModel
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest : AbstractViewModelTest() {

    @Test
    fun loginProcessDeath() {
        val fakeManageResource = FakeManageResource()
        val fakeLoginRunAsync = FakeRunAsync()
        val loginRepository = FakeLoginRepository()
        loginRepository.isFailure(true)

        viewModelScenario {
            LoginViewModel(
                savedStateHandle = createSavedStateHandle(),
                loginRepository = loginRepository,
                runAsync = fakeLoginRunAsync,
                manageResource = fakeManageResource,
            )
        }.use { scenario ->


            scenario.viewModel.loginIn()

            scenario.assertBeforeAndAfterProcessDeath {
                assertEquals(errorUiState, scenario.viewModel.loginUiState.value)
            }

            loginRepository.isFailure(false)

            scenario.viewModel.loginIn()

            scenario.assertBeforeAndAfterProcessDeath {
                assertEquals(LoginUiState.Loading, scenario.viewModel.loginUiState.value)
            }
            loginRepository.checkSavedToken("fakeToken")
        }
    }
}

private val errorUiState =
    LoginUiState.Initial(errorState = ErrorState.Error(message = "service unavailable"))

private class FakeLoginRepository : LoginRepository {

    private var mockedResult = Result.success("fakeToken")
    private var failureFlag = false
    private lateinit var savedToken: String
    override suspend fun userToken(): Result<String> {

        return if (failureFlag) {
            Result.failure(IllegalStateException("auth failed"))
        } else {
            mockedResult
        }

    }

    override suspend fun saveUserToken(token: String) {
        savedToken = token
    }

    fun isFailure(flag: Boolean) {
        failureFlag = flag
    }

    fun checkSavedToken(expectedSavedToken: String) {
        assertEquals(savedToken, expectedSavedToken)
    }
}

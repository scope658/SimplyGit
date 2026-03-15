package org.example.project.profile.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileViewModelTest {

    @Test
    fun `failure load profile then success`() = runBlocking {
        val profileRepository = FakeProfileRepository()
        profileRepository.isFailure(true)
        val runAsync = ControlledFakeRunAsync()

        val profileViewModel = ProfileViewModel(
            runAsync = runAsync,
            profileRepository = profileRepository
        )

        val profileUiState: StateFlow<ProfileUiState> = profileViewModel.profileUiState
        assertEquals(ProfileUiState.Loading, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(failureUiResult, profileUiState.value)

        profileRepository.isFailure(isFailure = false)
        profileViewModel.retry()

        assertEquals(ProfileUiState.Loading, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(successUiResult, profileUiState.value)

        val profileEvent: SharedFlow<ProfileEvent> = profileViewModel.profileEvent
        val job: Job = launch(Dispatchers.Unconfined) {
            profileEvent
                .collect {
                    assertEquals(ProfileEvent.Logout, it)
                }
        }

        profileViewModel.logout()
        profileRepository.checkLogoutIsCalled(1)
        runAsync.invokeUi()
        job.cancel()
    }

}

private val successUiResult = ProfileUiState.Success(
    avatar = "fakeImageUrl",
    userName = "scope",
    bio = "fakeBio",
    repoCount = "12",
    subscribersCount = "33"
)

private val failureUiResult = ProfileUiState.Failure(
    message = "fake message"
)

private val mockedSuccessResult = Profile(
    avatar = "fakeImageUrl",
    userName = "scope",
    bio = "fakeBio",
    repoCount = 12,
    subscribersCount = 33,
)

private class FakeProfileRepository : ProfileRepository {

    private var isFailure = false
    private var logoutCalledTimes = 0
    override suspend fun userProfile(): Result<Profile> {
        return if (isFailure) {
            Result.failure(IllegalStateException("fake message"))
        } else {
            Result.success(mockedSuccessResult)
        }
    }

    override suspend fun logout() {
        logoutCalledTimes++
    }

    fun isFailure(isFailure: Boolean) {
        this.isFailure = isFailure
    }

    fun checkLogoutIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, logoutCalledTimes)
    }
}

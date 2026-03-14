package org.example.project

import kotlinx.coroutines.flow.StateFlow
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.profile.domain.ProfileRepository
import org.example.project.profile.domain.UserProfile
import org.example.project.profile.presentation.ProfileUiState
import org.example.project.profile.presentation.ProfileViewModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileViewModelTest {

    @Test
    fun `failure load profile then success`() {
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

private val mockedSuccessResult = UserProfile(
    avatar = "fakeImageUrl",
    userName = "scope",
    bio = "fakeBio",
    repoCount = "12",
    subscribersCount = "33"
)

private class FakeProfileRepository : ProfileRepository {

    private var isFailure = false
    override suspend fun userProfile(): Result<UserProfile> {
        return if (isFailure) {
            Result.failure(IllegalStateException("fake message"))
        } else {
            Result.success(mockedSuccessResult)
        }
    }

    fun isFailure(isFailure: Boolean) {
        this.isFailure = isFailure
    }
}

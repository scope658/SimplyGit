package org.example.project.profile.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.core.domain.FakeManageResource
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileViewModelTest {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileRepository: FakeProfileRepository
    private lateinit var runAsync: ControlledFakeRunAsync
    private lateinit var profileUiMapper: Profile.Mapper<ProfileUiState>
    private lateinit var fakeManageResource: FakeManageResource

    @BeforeTest
    fun setUp() {
        fakeManageResource = FakeManageResource()
        profileUiMapper = ProfileUiMapper()
        runAsync = ControlledFakeRunAsync()
        profileRepository = FakeProfileRepository()
        profileViewModel = ProfileViewModel(
            runAsync,
            profileRepository,
            profileUiMapper,
            manageResource = fakeManageResource
        )
    }

    @Test
    fun `failure load profile then success`() = runBlocking {
        val profileRepository = FakeProfileRepository()
        profileRepository.isFailure(true)

        profileViewModel = ProfileViewModel(
            runAsync,
            profileRepository,
            profileUiMapper,
            manageResource = fakeManageResource,
        )

        val profileUiState: StateFlow<ProfileScreenState> = profileViewModel.profileUiState
        assertEquals(firstInitialState, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(failureUiResult, profileUiState.value)

        profileRepository.isFailure(isFailure = false)
        profileViewModel.retry()

        assertEquals(firstInitialState, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(
            successUiResult, profileUiState.value
        )
    }

    @Test
    fun `first run init block`() {
        val profileUiState: StateFlow<ProfileScreenState> = profileViewModel.profileUiState
        assertEquals(firstInitialState, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(
            successUiResult.copy(
                profileUiState = successResult.copy(
                    userName = "scope"
                ),
                isRefreshing = false,
            ), profileUiState.value
        )
    }

    @Test
    fun refresh() {
        val profileUiState = profileViewModel.profileUiState
        assertEquals(firstInitialState, profileUiState.value)

        runAsync.invokeUi()

        assertEquals(
            successUiResult.copy(
                profileUiState = successResult.copy(
                    userName = "scope"
                ),
                isRefreshing = false,
            ), profileUiState.value
        )

        profileViewModel.refresh()

        assertEquals(
            expected = successUiResult.copy(
                profileUiState = successResult.copy(
                    userName = "scope"
                ),
                isRefreshing = true,
            ),
            actual = profileUiState.value
        )
        runAsync.invokeUi()

        profileRepository.checkRefreshIsCalled(1)
        assertEquals(
            successUiResult.copy(
                isRefreshing = false,
            ), profileUiState.value
        )
    }

    @Test
    fun logout() = runBlocking {
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

private val firstInitialState = ProfileScreenState(
    isRefreshing = false,
    profileUiState = ProfileUiState.Loading,
)
private val successResult = ProfileUiState.Success(
    avatar = "fakeImageUrl",
    userName = "newUserName",
    bio = "fakeBio",
    repoCount = "12",
    subscribersCount = "33",
)
private val successUiResult = ProfileScreenState(
    isRefreshing = false,
    profileUiState = successResult,
)

val failureUiResult = ProfileScreenState(
    isRefreshing = false,
    ProfileUiState.Failure(
        message = "service unavailable"
    )

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
    private lateinit var mockedProfile: Profile
    private var refreshCalledTimes = 0
    private var mockedResult = mockedSuccessResult

    fun checkRefreshIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, refreshCalledTimes)
    }

    override suspend fun refreshUserProfile(): Result<Profile> {
        refreshCalledTimes++
        return if (isFailure) {
            Result.failure(IllegalStateException("fake message"))
        } else {
            Result.success(mockedResult.copy(userName = "newUserName"))
        }
    }

    override suspend fun logout() {
        logoutCalledTimes++
    }

    override suspend fun loadUserProfile(): Result<Profile> {
        return if (isFailure) {
            Result.failure(IllegalStateException("fake message"))
        } else {
            Result.success(mockedResult)
        }
    }

    fun isFailure(isFailure: Boolean) {
        this.isFailure = isFailure
    }

    fun checkLogoutIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, logoutCalledTimes)
    }

    fun mockProfileResult(result: Profile) {
        mockedResult = result
    }
}

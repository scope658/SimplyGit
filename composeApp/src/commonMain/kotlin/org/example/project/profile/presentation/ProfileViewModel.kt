package org.example.project.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.core.presentation.RunAsync
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository


class ProfileViewModel(
    private val runAsync: RunAsync,
    private val profileRepository: ProfileRepository,
    private val profileUiMapper: Profile.Mapper<ProfileUiState>,
    private val manageResource: ManageResource,
) : ViewModel(), ProfileActions {

    private val _profileUiState: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(
            value = ProfileScreenState(
                isRefreshing = false,
                profileUiState = ProfileUiState.Loading
            )
        )
    val profileUiState = _profileUiState.asStateFlow()

    private val _profileEvent: MutableSharedFlow<ProfileEvent> = MutableSharedFlow()
    val profileEvent = _profileEvent.asSharedFlow()

    init {
        loadProfile {
            profileRepository.loadUserProfile()
        }
    }

    override fun retry() {
        _profileUiState.update {
            it.copy(
                profileUiState = ProfileUiState.Loading
            )
        }
        loadProfile {
            profileRepository.refreshUserProfile()
        }
    }

    override fun refresh() {
        _profileUiState.update {
            it.copy(isRefreshing = true)
        }
        loadProfile {
            profileRepository.refreshUserProfile()
        }
    }

    override fun logout() {
        runAsync.runAsync(
            viewModelScope,
            background = { profileRepository.logout() },
            ui = { _profileEvent.emit(ProfileEvent.Logout) }
        )
    }

    private fun loadProfile(block: suspend () -> Result<Profile>) {

        runAsync.runAsync(
            scope = viewModelScope,
            background = block,
            ui = { result ->
                result
                    .onSuccess { userProfile ->
                        updateScreenState(
                            profileUiState = userProfile.mapSuccess(mapper = profileUiMapper)
                        )
                    }
                    .onFailure {
                        val error = it as? DomainException ?: ServiceUnavailableException

                        updateScreenState(
                            profileUiState = ProfileUiState.Failure(
                                message = error.exceptionString(manageResource = manageResource)
                            )
                        )
                    }
            },
        )
    }

    private fun updateScreenState(profileUiState: ProfileUiState) {
        _profileUiState.update {
            ProfileScreenState(
                isRefreshing = false,
                profileUiState = profileUiState,
            )
        }
    }
}

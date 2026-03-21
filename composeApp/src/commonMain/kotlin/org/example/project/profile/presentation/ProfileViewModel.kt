package org.example.project.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.RunAsync
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository


class ProfileViewModel(
    private val runAsync: RunAsync,
    private val profileRepository: ProfileRepository,
    private val profileUiMapper: Profile.Mapper<ProfileUiState>
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
        val profileUiState = _profileUiState.value
        runAsync.runAsync(
            scope = viewModelScope,
            background = block,
            ui = { result ->
                result
                    .onSuccess { userProfile ->
                        _profileUiState.value = profileUiState.copy(
                            profileUiState = userProfile.mapSuccess(mapper = profileUiMapper),
                            isRefreshing = false,
                        )
                    }
                    .onFailure {
                        _profileUiState.value = profileUiState.copy(
                            isRefreshing = false,
                            profileUiState = ProfileUiState.Failure(
                                message = it.message ?: HARDCODED_FAILURE
                            )
                        )
                    }
            },
        )
    }

    companion object {
        private const val HARDCODED_FAILURE = "something went wrong"
    }
}

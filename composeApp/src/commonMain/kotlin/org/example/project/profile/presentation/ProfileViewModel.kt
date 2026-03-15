package org.example.project.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.core.RunAsync
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository


class ProfileViewModel(
    private val runAsync: RunAsync,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileUiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(value = ProfileUiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()

    private val _profileEvent: MutableSharedFlow<ProfileEvent> = MutableSharedFlow()
    val profileEvent = _profileEvent.asSharedFlow()

    init {
        loadProfile()
    }

    fun retry() {
        loadProfile()
    }

    private fun loadProfile() {
        _profileUiState.value = ProfileUiState.Loading
        runAsync.runAsync(
            scope = viewModelScope,
            background = { profileRepository.userProfile() },
            ui = { result ->
                result
                    .onSuccess { userProfile ->
                        _profileUiState.value = userProfile.successToUi()
                    }
                    .onFailure {
                        _profileUiState.value =
                            ProfileUiState.Failure(message = it.message ?: HARDCODED_FAILURE)
                    }
            },
        )
    }


    fun logout() {
        runAsync.runAsync(
            viewModelScope,
            background = { profileRepository.logout() },
            ui = { _profileEvent.emit(ProfileEvent.Logout) }
        )
    }

    companion object {
        private const val HARDCODED_FAILURE = "something went wrong"
    }
}

fun Profile.successToUi() = ProfileUiState.Success(
    avatar = this.avatar,
    userName = this.userName,
    bio = this.bio,
    repoCount = this.repoCount.toString(),
    subscribersCount = this.subscribersCount.toString()
)

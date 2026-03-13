package org.example.project.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.example.project.app.domain.AppRepository
import org.example.project.core.RunAsync

class AppViewModel(private val runAsync: RunAsync, private val appRepository: AppRepository) :
    ViewModel() {
    private val _appEvent: MutableSharedFlow<AppEvent> = MutableSharedFlow(replay = 1)
    val appEvent: SharedFlow<AppEvent> = _appEvent.asSharedFlow()

    init {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                val isOnboarded = appRepository.isUserOnboarded()
                val isLoggedIn = appRepository.isTokenSaved()
                isOnboarded to isLoggedIn
            },
            ui = { (isOnboarded, isLoggedIn) ->
                when {
                    isOnboarded && isLoggedIn -> emitEvent(AppEvent.OnMain)
                    isOnboarded && !isLoggedIn -> emitEvent(AppEvent.OnLoginIn)
                    else -> emitEvent(AppEvent.OnOnboarding)
                }
            }
        )
    }

    private fun emitEvent(event: AppEvent) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {},
            ui = { _appEvent.emit(event) }
        )
    }
}

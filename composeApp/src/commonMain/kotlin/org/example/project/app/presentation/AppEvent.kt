package org.example.project.app.presentation

sealed class AppEvent {
    object OnMain : AppEvent()
    object OnOnboarding : AppEvent()
    object OnLoginIn : AppEvent()
}

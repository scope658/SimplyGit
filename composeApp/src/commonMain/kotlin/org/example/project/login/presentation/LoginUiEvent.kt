package org.example.project.login.presentation

sealed class LoginUiEvent {
    object LoginSuccessEvent : LoginUiEvent()
}
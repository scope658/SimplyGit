package org.example.project.profile.presentation

sealed class ProfileEvent {
    object Logout : ProfileEvent()
}

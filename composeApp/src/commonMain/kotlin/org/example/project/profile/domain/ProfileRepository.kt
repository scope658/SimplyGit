package org.example.project.profile.domain

interface ProfileRepository {
    suspend fun refreshUserProfile(): Result<Profile>
    suspend fun logout()
    suspend fun loadUserProfile(): Result<Profile>
}


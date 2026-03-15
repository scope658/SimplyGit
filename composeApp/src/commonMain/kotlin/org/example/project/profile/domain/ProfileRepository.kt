package org.example.project.profile.domain

interface ProfileRepository {
    suspend fun userProfile(): Result<Profile>
    suspend fun logout()
}


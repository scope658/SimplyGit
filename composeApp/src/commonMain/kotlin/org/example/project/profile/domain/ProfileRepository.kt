package org.example.project.profile.domain

interface ProfileRepository {
    suspend fun userProfile(): Result<UserProfile>
}

data class UserProfile(
    val avatar: String,
    val userName: String,
    val bio: String,
    val repoCount: String,
    val subscribersCount: String,

    )

package org.example.project.profile.data

data class ProfileData(
    val userId: Long,
    val avatar: String,
    val userName: String,
    val bio: String,
    val repoCount: Int,
    val subscribersCount: Int,
)

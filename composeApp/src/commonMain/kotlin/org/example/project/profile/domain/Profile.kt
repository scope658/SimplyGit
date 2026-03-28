package org.example.project.profile.domain

data class Profile(
    val avatar: String,
    val userName: String,
    val bio: String,
    val repoCount: Int,
    val subscribersCount: Int,
)

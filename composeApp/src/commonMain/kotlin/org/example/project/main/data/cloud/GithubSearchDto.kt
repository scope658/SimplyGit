package org.example.project.main.data.cloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubSearchDto(
    val items: List<RepoDto>
)

@Serializable
data class RepoDto(
    val id: Int,
    val name: String,
    val owner: OwnerDto,
    @SerialName("stargazers_count")
    val stars: Int,
    val language: String?
)

@Serializable
data class OwnerDto(
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
)

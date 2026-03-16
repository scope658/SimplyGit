package org.example.project.profile.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.profile.data.ProfileData

interface ProfileGithubApi {
    suspend fun userProfile(userToken: String): ProfileData

    class Base(private val httpClient: HttpClient) : ProfileGithubApi {
        override suspend fun userProfile(userToken: String): ProfileData {
            val response = httpClient.get("user") {
                header("Authorization", "Bearer $userToken")
                header("Accept", "application/vnd.github+json")
            }.body<GitHubUserDto>()

            return ProfileData(
                userId = response.id,
                avatar = response.avatarUrl,
                userName = response.login,
                bio = response.bio.orEmpty(),
                repoCount = response.publicRepos,
                subscribersCount = response.followers
            )
        }
    }
}

@Serializable
data class GitHubUserDto(
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String,
    val login: String,
    val bio: String?,
    @SerialName("public_repos")
    val publicRepos: Int,
    val followers: Int
)

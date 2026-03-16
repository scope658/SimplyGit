package org.example.project.main.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData>
    suspend fun userRepositories(page: Int): List<RepoData>

        override suspend fun userRepositories(page: Int, userToken: String): List<RepoData> {
            val response = httpClient.get("user/repos") {
                parameter("sort", "updated")
                parameter("per_page", 15)
                parameter("page", page)
                header("Authorization", "Bearer $userToken")
            }
            val items = response.body<List<RepoDto>>()
            return items.map { dto ->
                RepoData(
                    id = dto.id,
                    userPhotoImageUrl = dto.owner.avatarUrl,
                    userName = dto.owner.login,
                    repositoryName = dto.name,
                    programmingLanguage = dto.language ?: "Unknown",
                    stars = dto.stars
                )
            }
        }
    }
}

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


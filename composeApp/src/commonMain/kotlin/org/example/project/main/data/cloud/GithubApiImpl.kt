package org.example.project.main.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import org.example.project.main.data.RepoData

class GithubApiImpl(private val httpClient: HttpClient) : GithubApi {
    override suspend fun fetchByQuery(
        userQuery: String,
        page: Int,
        token: String,
    ): List<RepoData> {

        val response = httpClient.get("search/repositories") {
            parameter("q", userQuery)
            parameter("per_page", 15)
            parameter("page", page)
            header("Authorization", "Bearer $token")
        }
        val items = response.body<GithubSearchDto>().items
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

    override suspend fun userRepositories(token: String): List<RepoData> {

        val response = httpClient.get("user/repos") {
            parameter("sort", "updated")
            header("Authorization", "Bearer $token")
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

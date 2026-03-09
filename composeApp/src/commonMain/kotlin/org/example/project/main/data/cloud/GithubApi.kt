package org.example.project.main.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.MockData
import org.example.project.TokenStorage

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData>
    suspend fun userRepositories(page: Int): List<RepoData>

    class Base(private val httpClient: HttpClient) : GithubApi {

        override suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData> {
            val currentToken = TokenStorage.token
            val response = httpClient.get("search/repositories") {
                parameter("q", userQuery)
                parameter("per_page", 15)
                parameter("page", page)
                header("Authorization", "Bearer $currentToken")
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

        override suspend fun userRepositories(page: Int): List<RepoData> {
            val token = TokenStorage.token
            val response = httpClient.get("user/repos") {
                parameter("sort", "updated")
                parameter("per_page", 15)
                parameter("page", page)
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
}

class FakeGithubApi : GithubApi {
    private var exception: Exception? = null
    private var mockedSearchResult = MockData.mockedSearchDataRepositories
    private var mockedUserRepoResult = MockData.mockedUserDataRepositories
    override suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedSearchResult
    }

    override suspend fun userRepositories(page: Int): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedUserRepoResult
    }

    fun setException(exception: Exception?) {
        this.exception = exception
    }

    fun setEmptySearchResult() {
        mockedSearchResult = emptyList()
    }

    fun setEmptyUserRepoResult() {
        mockedUserRepoResult = emptyList()
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


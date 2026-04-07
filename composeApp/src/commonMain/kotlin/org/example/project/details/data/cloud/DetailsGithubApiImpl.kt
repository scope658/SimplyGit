package org.example.project.details.data.cloud

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.details.data.DetailsData
import org.example.project.details.data.ReadmeData
import org.example.project.details.domain.ReadmeNotFoundException

class DetailsGithubApiImpl(private val httpClient: HttpClient) : DetailsGithubApi {
    override suspend fun details(
        repoOwner: String,
        repoName: String
    ): DetailsData {
        val response = httpClient.get("repos/$repoOwner/$repoName") {}.body<GithubRepoDto>()

        Napier.d("details is pinged", tag = "ss221")
        return DetailsData(
            repoOwner = response.owner.login,
            repoName = response.name,
            repoDesc = response.description.orEmpty(),
            forksCount = response.forksCount,
            issuesCount = response.openIssuesCount,
            programmingLanguage = response.language.orEmpty()
        )
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): ReadmeData {
        val response = httpClient.get("repos/$repoOwner/$repoName/readme") {
            header("Accept", "application/vnd.github.v3.raw")
        }
        if (response.status == HttpStatusCode.NotFound) {
            throw ReadmeNotFoundException
        }
        return ReadmeData(
            repoOwner = repoOwner,
            repoName = repoName,
            readme = response.bodyAsText(),
        )
    }
}

@Serializable
data class GithubRepoDto(
    val name: String,
    val description: String?,
    @SerialName("forks_count")
    val forksCount: Int,
    val language: String?,

    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val owner: OwnerDto
) {
    @Serializable
    data class OwnerDto(
        val login: String
    )
}

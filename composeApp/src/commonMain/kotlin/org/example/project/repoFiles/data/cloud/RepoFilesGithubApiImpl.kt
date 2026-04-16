package org.example.project.repoFiles.data.cloud

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.example.project.repoFiles.data.RepoFilesData

class RepoFilesGithubApiImpl(private val httpClient: HttpClient) : RepoFilesGithubApi {
    override suspend fun repoFiles(
        repoOwner: String,
        repoName: String,
        path: String,
    ): List<RepoFilesData> {
        Napier.d("before response", tag = "ss21")
        val response =
            httpClient.get("https://api.github.com/repos/${repoOwner}/${repoName}/contents/${path}")
                .body<List<GitHubFileDto>>()
        Napier.d("after response and is $response", tag = "ss21")
        return response.map { gitHubFileDto ->
            RepoFilesData(
                name = gitHubFileDto.name,
                path = gitHubFileDto.path,
                type = gitHubFileDto.type.fileTypeData(),
                size = gitHubFileDto.size,
                downloadUrl = gitHubFileDto.correctDownloadUrl(),
            )
        }
    }
}


package org.example.project.createIssues.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.example.project.createIssues.data.IssueData
import org.example.project.createIssues.domain.IssuesDisabledException
import org.example.project.createIssues.domain.ServerValidationException

class IssueGithubApiImpl(private val httpClient: HttpClient) : IssueGithubApi {
    override suspend fun createIssue(
        repoOwner: String,
        repoName: String,
        issueData: IssueData
    ) {
        val response = httpClient.post("https://api.github.com/repos/$repoOwner/$repoName/issues") {
            contentType(ContentType.Application.Json)
            setBody(
                IssueRequest(
                    title = issueData.title,
                    body = issueData.body,
                )
            )
        }
        when (response.status) {
            HttpStatusCode.Created -> Unit
            HttpStatusCode.Gone -> throw IssuesDisabledException
            HttpStatusCode.UnprocessableEntity -> throw ServerValidationException
        }
    }
}

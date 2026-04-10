package org.example.project.createIssues.data.cloud

import org.example.project.createIssues.data.IssueData
import org.example.project.createIssues.domain.IssuesDisabledException
import org.example.project.details.data.cloud.FakeDetailsGithubApi

interface IssueGithubApi {
    suspend fun createIssue(repoOwner: String, repoName: String, issueData: IssueData)
}

class FakeIssueGithubApi(private val detailsGithubApi: FakeDetailsGithubApi) : IssueGithubApi {
    private var isFailure = false
    override suspend fun createIssue(
        repoOwner: String,
        repoName: String,
        issueData: IssueData
    ) {
        if (isFailure) {
            throw IssuesDisabledException
        } else {
            detailsGithubApi.incrementIssueCount()
        }
    }

    fun isFailure(flag: Boolean) {
        this.isFailure = flag
    }

}

package org.example.project.createIssues.data

import org.example.project.core.data.RunCatchingSuspend
import org.example.project.createIssues.data.cloud.IssueGithubApi
import org.example.project.createIssues.domain.Issue

import org.example.project.createIssues.domain.IssueRepository

class IssueRepositoryImpl(
    private val runCatchingSuspend: RunCatchingSuspend,
    private val issuesGithubApi: IssueGithubApi,
) : IssueRepository {

    override suspend fun createIssue(
        repoOwner: String,
        repoName: String,
        issue: Issue
    ): Result<Unit> {
        return runCatchingSuspend.catch {
            issuesGithubApi.createIssue(repoOwner, repoName, IssueData(issue.title, issue.body))
        }
    }
}

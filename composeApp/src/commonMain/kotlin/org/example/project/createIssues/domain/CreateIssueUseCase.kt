package org.example.project.createIssues.domain


interface CreateIssueUseCase {
    suspend fun createIssue(
        repoOwner: String,
        repoName: String,
        issue: Issue
    ): IssueResult
}



package org.example.project.createIssues.domain

interface IssueRepository {
    suspend fun createIssue(repoOwner: String, repoName: String, issue: Issue): Result<Unit>
}

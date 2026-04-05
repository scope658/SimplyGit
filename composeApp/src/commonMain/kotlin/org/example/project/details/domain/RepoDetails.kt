package org.example.project.details.domain

data class RepoDetails(
    val repoOwner: String,
    val repoName: String,
    val repoDesc: String,
    val forksCount: Int,
    val issuesCount: Int,
    val programmingLanguage: String,
)

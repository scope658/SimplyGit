package org.example.project.details.presentation

interface DetailsActions {
    fun retry()
    fun refresh()
    fun onCode(repoOwner: String, repoName: String)
    fun onCreateIssues(repoOwner: String, repoName: String)
}

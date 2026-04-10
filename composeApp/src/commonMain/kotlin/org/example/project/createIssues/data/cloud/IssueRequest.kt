package org.example.project.createIssues.data.cloud

import kotlinx.serialization.Serializable

@Serializable
data class IssueRequest(
    val title: String,
    val body: String,

    )

package org.example.project.createIssues.domain

interface IssuesManageResource {
    suspend fun longBody(currentBodyLength: Int): String
    suspend fun longTitle(currentLongTitleLength: Int): String
}

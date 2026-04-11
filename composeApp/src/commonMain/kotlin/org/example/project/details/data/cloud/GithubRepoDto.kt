package org.example.project.details.data.cloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GithubRepoDto(
    val name: String,
    val description: String?,
    @SerialName("forks_count")
    val forksCount: Int,
    val language: String?,

    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val owner: OwnerDto
) {
    @Serializable
    data class OwnerDto(
        val login: String
    )
}

package org.example.project.details.domain

data class RepoDetails(
    val repoOwner: String,
    val repoName: String,
    val repoDesc: String,
    val forksCount: Int,
    val issuesCount: Int,
    val programmingLanguage: String,
) {
    interface Mapper<T> {
        fun map(repoDetails: RepoDetails, readme: String): T
    }

    fun <T : Any> mapSuccess(mapper: Mapper<T>, readme: String): T {
        return mapper.map(this, readme)
    }
}

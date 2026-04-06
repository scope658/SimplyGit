package org.example.project.details.data

data class DetailsData(
    val repoOwner: String,
    val repoName: String,
    val repoDesc: String,
    val forksCount: Int,
    val issuesCount: Int,
    val programmingLanguage: String,
) {
    interface Mapper<T> {
        fun map(detailsData: DetailsData): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

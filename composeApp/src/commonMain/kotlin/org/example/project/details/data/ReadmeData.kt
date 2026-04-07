package org.example.project.details.data

data class ReadmeData(
    val repoOwner: String,
    val repoName: String,
    val readme: String,
) {
    interface Mapper<T> {
        fun map(readmeData: ReadmeData): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

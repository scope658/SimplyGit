package org.example.project.main.data

data class RepoData(
    val id: Int,
    val userPhotoImageUrl: String,
    val userName: String,
    val repositoryName: String,
    val programmingLanguage: String,
    val stars: Int,
) {
    interface Mapper<T> {
        fun map(
            repoData: RepoData
        ): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(
            this

        )
    }
}

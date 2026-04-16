package org.example.project.repoFiles.domain

data class RepoFiles(
    val name: String,
    val path: String,
    val type: FileType,
    val size: Long = 0,
    val downloadUrl: String,
) {
    interface Mapper<T> {
        fun map(repoFiles: RepoFiles): T
    }

    fun <T : Any> mapSuccess(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

sealed interface FileType {


    object File : FileType

    object Dir : FileType
    object SymLink : FileType
}

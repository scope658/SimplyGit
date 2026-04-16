package org.example.project.repoFiles.data

data class RepoFilesData(
    val name: String,
    val path: String,
    val type: FileTypeData,
    val size: Long = 0,
    val downloadUrl: String,
) {
    interface Mapper<T> {
        fun map(repoFilesData: RepoFilesData): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

interface FileTypeData {

    fun <T : Any> fileType(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun mapFile(): T
        fun mapDir(): T
        fun mapSymlink(): T
    }

    object File : FileTypeData {
        override fun <T : Any> fileType(mapper: Mapper<T>): T {
            return mapper.mapFile()
        }

    }

    object Dir : FileTypeData {
        override fun <T : Any> fileType(mapper: Mapper<T>): T {
            return mapper.mapDir()
        }
    }

    object Symlink : FileTypeData {
        override fun <T : Any> fileType(mapper: Mapper<T>): T {
            return mapper.mapSymlink()
        }
    }
}

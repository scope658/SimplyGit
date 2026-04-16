package org.example.project.repoFiles.data.cloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.repoFiles.data.FileTypeData

@Serializable
data class GitHubFileDto(
    val name: String,
    val path: String,
    val type: FileTypDto,
    val size: Long = 0,
    @SerialName("download_url")
    val downloadUrl: String? = null,
) {
    fun correctDownloadUrl(): String {
        return when (type) {
            FileTypDto.Dir -> ""

            else -> downloadUrl ?: ""
        }
    }
}

@Serializable
enum class FileTypDto {
    @SerialName("file")
    File,

    @SerialName("dir")
    Dir,

    @SerialName("symlink")
    Symlink;

    fun fileTypeData(): FileTypeData = when (this) {
        File -> FileTypeData.File
        Dir -> FileTypeData.Dir
        Symlink -> FileTypeData.Symlink
    }
}

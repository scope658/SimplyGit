package org.example.project.repoFiles.presentation

import org.example.project.repoFiles.domain.FileType
import org.example.project.repoFiles.domain.RepoFiles

class RepoFilesUiMapper : RepoFiles.Mapper<RepoFileUi> {
    override fun map(repoFiles: RepoFiles): RepoFileUi = with(repoFiles) {
        return RepoFileUi(
            name = name,
            fileType = mapFileType(type),
            path = path,
            downloadUrl = downloadUrl,
            size = size
        )
    }

    private fun mapFileType(fileType: FileType): FileTypeUi {
        return when (fileType) {
            FileType.Dir -> FileTypeUi.Dir
            FileType.File -> FileTypeUi.File
            FileType.SymLink -> FileTypeUi.SymLink
        }
    }
}

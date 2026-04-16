package org.example.project.repoFiles.data

import org.example.project.repoFiles.domain.FileType
import org.example.project.repoFiles.domain.RepoFiles

class RepoFilesDataToDomain(private val fileTypeMapper: FileTypeData.Mapper<FileType>) :
    RepoFilesData.Mapper<RepoFiles> {
    override fun map(repoFilesData: RepoFilesData): RepoFiles = with(repoFilesData) {
        return RepoFiles(
            name = name,
            path = path,
            type = type.fileType(fileTypeMapper),
            size = size,
            downloadUrl = downloadUrl,
        )
    }
}

class FileTypeMapper : FileTypeData.Mapper<FileType> {
    override fun mapFile(): FileType {
        return FileType.File
    }

    override fun mapDir(): FileType {
        return FileType.Dir
    }

    override fun mapSymlink(): FileType {
        return FileType.SymLink
    }
}

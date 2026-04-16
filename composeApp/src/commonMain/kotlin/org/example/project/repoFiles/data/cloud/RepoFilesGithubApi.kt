package org.example.project.repoFiles.data.cloud

import org.example.project.repoFiles.data.RepoFilesData

interface RepoFilesGithubApi {

    suspend fun repoFiles(repoOwner: String, repoName: String, path: String): List<RepoFilesData>

}

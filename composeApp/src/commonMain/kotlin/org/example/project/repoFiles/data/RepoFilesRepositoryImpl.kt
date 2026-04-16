package org.example.project.repoFiles.data

import org.example.project.core.data.RunCatchingSuspend
import org.example.project.repoFiles.data.cloud.RepoFilesGithubApi
import org.example.project.repoFiles.domain.RepoFiles
import org.example.project.repoFiles.domain.RepoFilesRepository

class RepoFilesRepositoryImpl(
    private val repoFilesGithubApi: RepoFilesGithubApi,
    private val repoFilesDataToDomain: RepoFilesData.Mapper<RepoFiles>,
    private val runCatchingSuspend: RunCatchingSuspend,
) : RepoFilesRepository {
    override suspend fun repoFiles(
        repoOwner: String,
        repoName: String,
        path: String
    ): Result<List<RepoFiles>> {
        return runCatchingSuspend.catch {
            val data = repoFilesGithubApi.repoFiles(repoOwner, repoName, path)
            data.map { repoFilesData ->
                repoFilesData.map(repoFilesDataToDomain)
            }
        }
    }
}

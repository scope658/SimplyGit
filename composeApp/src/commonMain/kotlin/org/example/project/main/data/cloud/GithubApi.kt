package org.example.project.main.data.cloud

import org.example.project.main.data.RepoData

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData>
    suspend fun userRepositories(): List<RepoData>

}

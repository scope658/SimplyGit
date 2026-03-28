package org.example.project.main.data.cloud

import org.example.project.main.data.RepoData

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int, token: String): List<RepoData>
    suspend fun userRepositories(token: String): List<RepoData>
}

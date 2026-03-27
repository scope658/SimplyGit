package org.example.project.main.data.cloud

import org.example.project.MockData
import org.example.project.main.data.RepoData

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData>
    suspend fun userRepositories(page: Int): List<RepoData>

}

class FakeGithubApi : GithubApi {
    private var exception: Exception? = null
    private var mockedSearchResult = MockData.mockedSearchDataRepositories
    private var mockedUserRepoResult = MockData.mockedUserDataRepositories
    override suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedSearchResult
    }

    override suspend fun userRepositories(page: Int): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedUserRepoResult
    }

    fun setException(exception: Exception?) {
        this.exception = exception
    }

    fun setEmptySearchResult() {
        mockedSearchResult = emptyList()
    }

    fun setEmptyUserRepoResult() {
        mockedUserRepoResult = emptyList()
    }
}

package org.example.project.main.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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

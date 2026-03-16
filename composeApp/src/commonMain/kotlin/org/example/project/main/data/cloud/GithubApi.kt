package org.example.project.main.data.cloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GithubApi {
    suspend fun fetchByQuery(userQuery: String, page: Int): List<RepoData>
    suspend fun userRepositories(page: Int): List<RepoData>

}

class FakeGithubApi : GithubApi, ProfileGithubApi {

    private var exception: Exception? = null
    private var mockedSearchResult = MockData.mockedSearchDataRepositories
    private var mockedUserRepoResult = MockData.mockedUserDataRepositories
    private var profileIsFailureFlag = false
    override suspend fun fetchByQuery(
        userQuery: String,
        page: Int,
        userToken: String
    ): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedSearchResult
    }

    override suspend fun userRepositories(page: Int, userToken: String): List<RepoData> {
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


    override suspend fun userProfile(userToken: String): ProfileData {
        if (profileIsFailureFlag) {
            throw IllegalStateException("something went wrong")
        } else {
            return ProfileData(
                userId = 1L,
                avatar = "fake avatar",
                userName = "scope",
                bio = "fake bio",
                repoCount = 12,
                subscribersCount = 23
            )
        }
    }

    fun isUserProfileIsFailure(profileFailureFlag: Boolean) {
        profileIsFailureFlag = profileFailureFlag
    }
}

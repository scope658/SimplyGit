package org.example.project.core.cloud

import org.example.project.MockData
import org.example.project.main.data.RepoData
import org.example.project.main.data.cloud.GithubApi

import org.example.project.profile.data.ProfileData
import org.example.project.profile.data.cloud.ProfileGithubApi

class FakeGithubApi : GithubApi, ProfileGithubApi {

    private var exception: Exception? = null
    private var mockedSearchResult = MockData.mockedSearchDataRepositories
    private var mockedUserRepoResult = MockData.mockedUserDataRepositories
    private var profileIsFailureFlag = false
    override suspend fun fetchByQuery(
        userQuery: String,
        page: Int,
        token: String
    ): List<RepoData> {
        exception?.let {
            throw it
        }
        return mockedSearchResult
    }

    override suspend fun userRepositories(token: String): List<RepoData> {
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

package org.example.project

import org.example.project.main.data.cloud.RepoData
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.UserRepositoryUi

object MockData {
    val mockedUserRepositoriesUi = listOf(
        UserRepositoryUi(
            id = 1,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "zeroHero",
            programmingLanguage = "kotlin",
            stars = 15,
        ),
        UserRepositoryUi(
            id = 2,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "homeWork",
            programmingLanguage = "kotlin",
            stars = 11,
        ),
        UserRepositoryUi(
            id = 3,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "android",
            programmingLanguage = "kotlin",
            stars = 300,
        ),
        UserRepositoryUi(
            id = 4,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "practice",
            programmingLanguage = "kotlin",
            stars = 1,
        ),
        UserRepositoryUi(
            id = 5,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "finUp",
            programmingLanguage = "kotlin",
            stars = 2,
        ),
        UserRepositoryUi(
            id = 6,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "match color",
            programmingLanguage = "kotlin",
            stars = 5,
        ),
        UserRepositoryUi(
            id = 7,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "hero",
            programmingLanguage = "kotlin",
            stars = 9,
        ),
        UserRepositoryUi(
            id = 8,
            userPhotoImageUrl = "fake",
            userName = "scope658",
            repositoryName = "repo",
            programmingLanguage = "kotlin",
            stars = 123,
        ),
    )
    val mockedRepositories = mockedUserRepositoriesUi.map {
        UserRepository(
            id = it.id,
            userPhotoImageUrl = it.userPhotoImageUrl,
            userName = it.userName,
            repositoryName = it.repositoryName,
            programmingLanguage = it.programmingLanguage,
            stars = it.stars,
        )
    }

    val mockedSearchResults = listOf(
        UserRepository(
            id = 101,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_1",
            repositoryName = "search-result-alpha",
            programmingLanguage = "Kotlin",
            stars = 5,
        ),
        UserRepository(
            id = 102,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_2",
            repositoryName = "test-repo-beta",
            programmingLanguage = "Java",
            stars = 12,
        ),
        UserRepository(
            id = 103,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_3",
            repositoryName = "sample-project-gamma",
            programmingLanguage = "Kotlin",
            stars = 0,
        ),
        UserRepository(
            id = 104,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_4",
            repositoryName = "demo-app-delta",
            programmingLanguage = "Swift",
            stars = 8,
        ),
        UserRepository(
            id = 105,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_5",
            repositoryName = "simple-tool-epsilon",
            programmingLanguage = "Kotlin",
            stars = 21,
        ),
        UserRepository(
            id = 106,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_6",
            repositoryName = "utility-zeta",
            programmingLanguage = "Python",
            stars = 3,
        ),
        UserRepository(
            id = 107,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_7",
            repositoryName = "helper-eta",
            programmingLanguage = "Kotlin",
            stars = 15,
        ),
        UserRepository(
            id = 108,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_8",
            repositoryName = "core-library-theta",
            programmingLanguage = "Kotlin",
            stars = 7,
        ),
        UserRepository(
            id = 109,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_9",
            repositoryName = "module-iota",
            programmingLanguage = "TypeScript",
            stars = 1,
        ),
        UserRepository(
            id = 110,
            userPhotoImageUrl = "fakePhoto",
            userName = "fakeUser_10",
            repositoryName = "last-result-kappa",
            programmingLanguage = "Kotlin",
            stars = 9,
        )
    )
    val mockedSearchRepositoriesUi = mockedSearchResults.map {
        UserRepositoryUi(
            id = it.id,
            userPhotoImageUrl = it.userPhotoImageUrl,
            userName = it.userName,
            repositoryName = it.repositoryName,
            programmingLanguage = it.programmingLanguage,
            stars = it.stars
        )
    }
    val mockedSearchDataRepositories = mockedSearchResults.map {
        RepoData(
            id = it.id,
            userPhotoImageUrl = it.userPhotoImageUrl,
            userName = it.userName,
            repositoryName = it.repositoryName,
            programmingLanguage = it.programmingLanguage,
            stars = it.stars
        )
    }
    val mockedUserDataRepositories = mockedRepositories.map {
        RepoData(
            id = it.id,
            userPhotoImageUrl = it.userPhotoImageUrl,
            userName = it.userName,
            repositoryName = it.repositoryName,
            programmingLanguage = it.programmingLanguage,
            stars = it.stars
        )
    }
}


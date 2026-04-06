package org.example.project.details

import kotlinx.coroutines.runBlocking
import org.example.project.core.data.HandleDomainError
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.details.data.DetailsData
import org.example.project.details.data.DetailsDataToDomain
import org.example.project.details.data.DetailsRepositoryImpl
import org.example.project.details.data.ReadmeData
import org.example.project.details.data.cache.DetailsCacheDataSource
import org.example.project.details.data.cloud.DetailsGithubApi
import org.example.project.details.domain.DetailsRepository
import org.example.project.details.domain.RepoDetails
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailsRepositoryTest {

    private lateinit var repository: DetailsRepository
    private lateinit var detailsCacheDataSource: FakeDetailsCacheDataSource
    private lateinit var detailsGithubApi: FakeDetailsGithubApi

    @BeforeTest
    fun setup() {
        detailsCacheDataSource = FakeDetailsCacheDataSource()
        val detailsDataToDomain: DetailsData.Mapper<RepoDetails> = DetailsDataToDomain()
        detailsGithubApi = FakeDetailsGithubApi()
        repository = DetailsRepositoryImpl(
            detailsCacheDataSource = detailsCacheDataSource,
            detailsDataToDomain = detailsDataToDomain,
            detailsGithubApi = detailsGithubApi,
            handleDomainError = HandleDomainError.Base()
        )
    }

    @Test
    fun `success refresh details then cache`() = runBlocking {
        detailsGithubApi.isDetailsFailure(false)

        val detailsActualResult = repository.refreshDetails("scope", "repo name")
        val expectedResult = Result.success(
            expectedRepoDetails
        )

        detailsGithubApi.checkDetailsIsCalled(expectedTimes = 1)
        detailsCacheDataSource.checkSaveDetailsIsCalled(listOf(expectedDetailsData))
        assertEquals(expectedResult, detailsActualResult)
    }

    @Test
    fun `failure refresh details but cache contains`() = runBlocking {
        detailsGithubApi.isDetailsFailure(true)
        detailsCacheDataSource.saveDetails(expectedDetailsData)

        val actualResult = repository.refreshDetails("scope", "repo name")
        val expectedResult =
            Result.success(expectedRepoDetails)

        detailsGithubApi.checkDetailsIsCalled(expectedTimes = 1)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `failure refresh and cache doesnt contains`() = runBlocking { //----
        detailsGithubApi.isDetailsFailure(true)


        repository.refreshDetails("123", "123")
            .onFailure {
                val exception = it as DomainException
                assertEquals(ServiceUnavailableException, exception)
            }
        detailsGithubApi.checkDetailsIsCalled(expectedTimes = 1)
    }

    @Test
    fun `repo details trigger refresh if cache doesnt contains`() = runBlocking {
        detailsGithubApi.isDetailsFailure(false)

        val actualResult = repository.repoDetails("scope", "repo name")
        val expectedResult = Result.success(expectedRepoDetails)

        assertEquals(expectedResult, actualResult)
        detailsGithubApi.checkDetailsIsCalled(expectedTimes = 1)
        detailsCacheDataSource.checkSaveDetailsIsCalled(listOf(expectedDetailsData))
    }

    @Test
    fun `repo details cache contains`() = runBlocking {
        detailsGithubApi.isDetailsFailure(true)
        detailsCacheDataSource.saveDetails(expectedDetailsData)

        val actualResult = repository.repoDetails("scope", "repo name")
        val expectedResult =
            Result.success(expectedRepoDetails)

        detailsGithubApi.checkDetailsIsCalled(expectedTimes = 0)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `success readme refresh then cache`() = runBlocking {
        detailsGithubApi.isReadmeFailure(false)

        val detailsActualResult = repository.refreshReadme("scope", "repo name")
        val expectedResult = Result.success(
            "readme content"
        )

        detailsGithubApi.checkReadmeIsCalled(1)
        detailsCacheDataSource.checkSaveReadmeIsCalled(listOf(expectedReadmeData))
        assertEquals(expectedResult, detailsActualResult)
    }

    @Test
    fun `failure refresh readme but cache contains`() = runBlocking {
        detailsGithubApi.isReadmeFailure(true)
        detailsCacheDataSource.saveReadme(readmeData = expectedReadmeData)

        val actualResult = repository.refreshReadme("scope", "repo name")
        val expectedResult =
            Result.success("readme content")

        assertEquals(expectedResult, actualResult)
        detailsGithubApi.checkReadmeIsCalled(1)
    }

    @Test
    fun `failure refresh readme and cache doesnt contains`() = runBlocking {
        detailsGithubApi.isReadmeFailure(true)

        repository.refreshReadme("123", "123")
            .onFailure {
                val exception = it as DomainException
                assertEquals(ServiceUnavailableException, exception)
            }
        detailsGithubApi.checkReadmeIsCalled(1)
    }


    @Test
    fun `readme trigger refresh readme if cache doesnt contains`() = runBlocking {
        detailsGithubApi.isReadmeFailure(false)

        val actualResult = repository.readme("scope", "repo name")
        val expectedResult = Result.success("readme content")

        assertEquals(expectedResult, actualResult)
        detailsGithubApi.checkReadmeIsCalled(expectedTimes = 1)
        detailsCacheDataSource.checkSaveReadmeIsCalled(listOf(expectedReadmeData))
    }

    @Test
    fun `readme cache contains`() = runBlocking {
        detailsGithubApi.isDetailsFailure(true)
        detailsCacheDataSource.saveReadme(expectedReadmeData)

        val actualResult = repository.readme("scope", "repo name")
        val expectedResult =
            Result.success("readme content")

        detailsGithubApi.checkReadmeIsCalled(expectedTimes = 0)
        assertEquals(expectedResult, actualResult)
    }

}

private val expectedRepoDetails = RepoDetails(
    repoOwner = "scope",
    repoName = "repo name",
    repoDesc = "repo dsec",
    forksCount = 1,
    issuesCount = 1,
    programmingLanguage = "kotlin",
)
private val expectedDetailsData = DetailsData(
    repoOwner = "scope",
    repoName = "repo name",
    repoDesc = "repo dsec",
    forksCount = 1,
    issuesCount = 1,
    programmingLanguage = "kotlin",
)
private val expectedReadmeData = ReadmeData(
    repoOwner = "scope",
    repoName = "repo name",
    readme = "readme content",
)

private class FakeDetailsGithubApi : DetailsGithubApi {

    private var detailsFailure = false
    private var isReadmeFailure = false
    private var detailsCalledTimes = 0
    private var readmeCalledTimes = 0

    fun isDetailsFailure(flag: Boolean) {
        detailsFailure = flag
    }

    fun isReadmeFailure(flag: Boolean) {
        isReadmeFailure = flag
    }

    override suspend fun details(repoOwner: String, repoName: String): DetailsData {
        detailsCalledTimes++
        if (detailsFailure) {
            throw IllegalStateException()
        } else {
            return expectedDetailsData
        }
    }

    override suspend fun readme(repoOwner: String, repoName: String): ReadmeData {
        readmeCalledTimes++
        if (isReadmeFailure) {
            throw IllegalStateException()
        } else {
            return expectedReadmeData
        }
    }

    fun checkDetailsIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, detailsCalledTimes)
    }

    fun checkReadmeIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, readmeCalledTimes)
    }
}

private class FakeDetailsCacheDataSource : DetailsCacheDataSource {


    private var readmeList = mutableListOf<ReadmeData>()
    private var detailsList = mutableListOf<DetailsData>()

    override suspend fun isReadmeContains(repoOwner: String, repoName: String): Boolean {
        return readmeList.find { it.repoOwner == repoOwner && it.repoName == repoName } != null
    }

    override suspend fun isDetailsContains(repoOwner: String, repoName: String): Boolean {
        return detailsList.find { it.repoOwner == repoOwner && it.repoName == repoName } != null
    }

    override suspend fun details(repoOwner: String, repoName: String): DetailsData {
        return detailsList.find { it.repoOwner == repoOwner && it.repoName == repoName }!!
    }

    override suspend fun readme(repoOwner: String, repoName: String): ReadmeData {
        return readmeList.find { it.repoOwner == repoOwner && it.repoName == repoName }!!
    }

    override suspend fun saveReadme(readmeData: ReadmeData) {
        readmeList.add(readmeData)
    }

    override suspend fun saveDetails(detailsData: DetailsData) {
        detailsList.add(detailsData)
    }

    fun checkSaveReadmeIsCalled(expectedReadmeList: List<ReadmeData>) {
        assertEquals(expectedReadmeList, readmeList)
    }

    fun checkSaveDetailsIsCalled(expectedDetailsList: List<DetailsData>) {
        assertEquals(expectedDetailsList, detailsList)
    }
}

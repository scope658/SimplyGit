package org.example.project.details.domain

import kotlinx.coroutines.runBlocking
import org.example.project.core.domain.FakeManageResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RepoDetailsUseCaseTest {

    private lateinit var repoDetailsUseCase: RepoDetailsUseCase
    private lateinit var detailsRepository: FakeDetailsRepository

    @BeforeTest
    fun setup() {
        detailsRepository = FakeDetailsRepository()
        val handleDetailsRequest: HandleDetailsRequest =
            HandleDetailsRequest(manageResource = FakeManageResource())
        repoDetailsUseCase = RepoDetailsUseCaseImpl(
            handleDetailsRequest = handleDetailsRequest,
            detailsRepository = detailsRepository,
        )
    }

    @Test
    fun `success details and readme`() = runBlocking {
        detailsRepository.detailsFailure(false)
        detailsRepository.readmeFailure(false)

        val actualResult =
            repoDetailsUseCase.repoDetails(repoOwner = "scope", repoName = "repo name")
        val expectedResult = CombinedDetailsResult.Success(
            repoDetails = expectedRepoDetails,
            readme = "readme success"
        )


        detailsRepository.checkDetailsCalled(expectedReadmeTimes = 1, expectedDetailsTimes = 1)
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `success details but readme failure`() = runBlocking {
        detailsRepository.detailsFailure(false)
        detailsRepository.readmeFailure(true)

        val actualResult = repoDetailsUseCase.repoDetails("scope", "repo name")
        val expectedResult = CombinedDetailsResult.Success(
            repoDetails = expectedRepoDetails,
            readme = "readme not found"
        )

        detailsRepository.checkDetailsCalled(expectedReadmeTimes = 1, expectedDetailsTimes = 1)
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `failure details`() = runBlocking {
        detailsRepository.detailsFailure(true)
        detailsRepository.readmeFailure(false)

        val actualResult = repoDetailsUseCase.repoDetails("scope", "repo name")
        val expectedResult = CombinedDetailsResult.Failure("service unavailable")

        detailsRepository.checkDetailsCalled(expectedReadmeTimes = 1, expectedDetailsTimes = 1)
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `success refresh details and readme`() = runBlocking {
        detailsRepository.detailsFailure(false)
        detailsRepository.readmeFailure(false)

        val actualResult =
            repoDetailsUseCase.refreshRepoDetails(repoOwner = "scope", repoName = "repo name")
        val expectedResult = CombinedDetailsResult.Success(
            repoDetails = expectedRepoDetails.copy(forksCount = 150),
            readme = "readme refresh success"
        )

        detailsRepository.checkRefreshCalled(
            expectedReadmeRefreshTimes = 1,
            expectedDetailsRefreshTimes = 1
        )
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `success refresh details but readme failure`() = runBlocking {
        detailsRepository.detailsFailure(false)
        detailsRepository.readmeFailure(true)

        val actualResult = repoDetailsUseCase.refreshRepoDetails("scope", "repo name")
        val expectedResult = CombinedDetailsResult.Success(
            repoDetails = expectedRepoDetails.copy(forksCount = 150),
            readme = "readme not found"
        )

        detailsRepository.checkRefreshCalled(
            expectedReadmeRefreshTimes = 1,
            expectedDetailsRefreshTimes = 1
        )
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `failure refresh details`() = runBlocking {
        detailsRepository.detailsFailure(true)
        detailsRepository.readmeFailure(false)

        val actualResult = repoDetailsUseCase.refreshRepoDetails("scope", "repo name")
        val expectedResult = CombinedDetailsResult.Failure("service unavailable")

        detailsRepository.checkRefreshCalled(
            expectedReadmeRefreshTimes = 1,
            expectedDetailsRefreshTimes = 1
        )
        assertEquals(expectedResult, actualResult)
    }
}

private val expectedRepoDetails = RepoDetails(
    repoOwner = "scope",
    repoName = "repo name",
    repoDesc = "repo desc",
    forksCount = 2,
    issuesCount = 2,
    programmingLanguage = "kotlin"
)


private class FakeDetailsRepository : DetailsRepository {

    private var isDetailsFailure = false
    private var isReadmeFailure = false

    private var detailsRefreshCalledTimes = 0
    private var readmeRefreshCalledTimes = 0

    private var detailsCalledTimes = 0
    private var readmeCalledTimes = 0

    override suspend fun repoDetails(
        repoOwner: String,
        repoName: String
    ): Result<RepoDetails> {
        detailsCalledTimes++
        return if (isDetailsFailure) {
            Result.failure(IllegalStateException(""))
        } else {
            Result.success(expectedRepoDetails)
        }
    }

    override suspend fun readme(
        repoOwner: String,
        repoName: String
    ): Result<String> {
        readmeCalledTimes++
        return if (isReadmeFailure) {
            Result.failure(ReadmeNotFoundException)
        } else {
            Result.success("readme success")
        }
    }

    override suspend fun refreshReadme(
        repoOwner: String,
        repoName: String
    ): Result<String> {
        readmeRefreshCalledTimes++
        return if (isReadmeFailure) {
            Result.failure(ReadmeNotFoundException)
        } else {
            Result.success("readme refresh success")
        }
    }

    override suspend fun refreshDetails(
        repoOwner: String,
        repoName: String
    ): Result<RepoDetails> {
        detailsRefreshCalledTimes++
        return if (isDetailsFailure) {
            Result.failure(IllegalStateException(""))
        } else {
            Result.success(expectedRepoDetails.copy(forksCount = 150))
        }
    }

    fun readmeFailure(flag: Boolean) {
        this.isReadmeFailure = flag
    }

    fun detailsFailure(flag: Boolean) {
        this.isDetailsFailure = flag
    }

    fun checkRefreshCalled(expectedReadmeRefreshTimes: Int, expectedDetailsRefreshTimes: Int) {
        assertEquals(expectedReadmeRefreshTimes, readmeRefreshCalledTimes)
        assertEquals(expectedDetailsRefreshTimes, detailsRefreshCalledTimes)
    }

    fun checkDetailsCalled(expectedReadmeTimes: Int, expectedDetailsTimes: Int) {
        assertEquals(expectedReadmeTimes, readmeCalledTimes)
        assertEquals(expectedDetailsTimes, detailsCalledTimes)
    }

}

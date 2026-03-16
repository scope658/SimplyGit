package org.example.project.main.domain

import kotlinx.coroutines.runBlocking
import org.example.project.MockData
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPagedReposUseCaseTest {

    private lateinit var getPagedReposUseCase: GetPagedReposUseCase
    private lateinit var mainRepository: FakeMainRepository
    private lateinit var handleMainRequest: HandleMainRequest

    @BeforeTest
    fun setUp() {
        handleMainRequest = HandleMainRequest.Base()
        mainRepository = FakeMainRepository()
        getPagedReposUseCase =
            GetPagedReposUseCase.Base(
                repository = mainRepository,
                handleMainRequest = handleMainRequest
            )
    }


    @Test
    fun `success load user repo with edge cases`() = runBlocking {
        val firstExpectedRepoList = MockData.mockForUseCaseTest.take(DEFAULT_PAGE_SIZE)
        mainRepository.mockResult(mockedResult = firstExpectedRepoList) //size = 15


        var actualResult = getPagedReposUseCase.userRepo(
            emptyList(),
            page = 1
        )
        var expectedResult = PagedResult.Success(
            page = 2,
            isPagingException = false,
            isLoadMore = true,
            repos = firstExpectedRepoList,
        )
        assertEquals(expectedResult, actualResult)

        mainRepository.mockResult(mockedResult = MockData.mockForUseCaseTest.take(INCOMPLETE_PAGE)) //size 14
        val secondExpectedRepoList =
            firstExpectedRepoList + MockData.mockForUseCaseTest.take(INCOMPLETE_PAGE)

        actualResult = getPagedReposUseCase.userRepo(firstExpectedRepoList, page = 2)
        expectedResult = PagedResult.Success(
            page = 3,
            isPagingException = false,
            isLoadMore = false,
            repos = secondExpectedRepoList,
        )
        assertEquals(expectedResult, actualResult)

    }

    @Test
    fun `success user query with edge cases`() = runBlocking {
        val firstExpectedRepoList = MockData.mockForUseCaseTest.take(DEFAULT_PAGE_SIZE)
        mainRepository.mockResult(mockedResult = firstExpectedRepoList) //size = 15


        var actualResult = getPagedReposUseCase.searchByQuery(
            emptyList(),
            userQuery = QUERY_EXAMPLE,
            page = FIRST_PAGE
        )
        var expectedResult = PagedResult.Success(
            page = SECOND_PAGE,
            isPagingException = false,
            isLoadMore = true,
            repos = firstExpectedRepoList,
        )
        assertEquals(expectedResult, actualResult)

        mainRepository.mockResult(mockedResult = MockData.mockForUseCaseTest.take(INCOMPLETE_PAGE)) //size 14
        val secondExpectedRepoList =
            firstExpectedRepoList + MockData.mockForUseCaseTest.take(INCOMPLETE_PAGE)

        actualResult =
            getPagedReposUseCase.searchByQuery(firstExpectedRepoList, QUERY_EXAMPLE, page = 2)
        expectedResult = PagedResult.Success(
            page = THIRD_PAGE,
            isPagingException = false,
            isLoadMore = false,
            repos = secondExpectedRepoList,
        )
        assertEquals(expectedResult, actualResult)

    }

    @Test
    fun `success load user repo then failure paging`() = runBlocking {
        val expectedRepoList = MockData.mockForUseCaseTest.take(DEFAULT_PAGE_SIZE)
        mainRepository.mockResult(mockedResult = expectedRepoList) //size = 15

        var actualResult = getPagedReposUseCase.userRepo(emptyList(), page = 1)
        var expectedResult = PagedResult.Success(
            page = SECOND_PAGE,
            isPagingException = false,
            isLoadMore = true,
            repos = expectedRepoList,
        )
        assertEquals(expectedResult, actualResult)

        mainRepository.mockFailure(true)

        actualResult = getPagedReposUseCase.userRepo(expectedRepoList, 2)
        expectedResult = PagedResult.Success(
            page = SECOND_PAGE,
            isPagingException = true,
            isLoadMore = true,
            repos = expectedRepoList
        )
        assertEquals(expectedResult, actualResult)
    }

    companion object {
        private const val QUERY_EXAMPLE = "QUERY"
        private const val DEFAULT_PAGE_SIZE = 15
        private const val INCOMPLETE_PAGE = 14
        private const val FIRST_PAGE = 1
        private const val SECOND_PAGE = 2
        private const val THIRD_PAGE = 3
    }
}

private class FakeMainRepository : MainRepository {

    private var mockedResult = MockData.mockedSearchResults
    private var isFailure = false
    override suspend fun userRepo(page: Int): Result<List<UserRepository>> {

        if (isFailure) {
            return Result.failure<List<UserRepository>>(IllegalStateException("something went wrong"))
        } else {
            return Result.success(mockedResult)
        }
    }

    override suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>> {
        if (isFailure) {
            return Result.failure<List<UserRepository>>(IllegalStateException("something went wrong"))

        } else {
            return Result.success(mockedResult)
        }
    }

    fun mockResult(mockedResult: List<UserRepository>) {
        this.mockedResult = mockedResult
    }

    fun mockFailure(failure: Boolean) {
        isFailure = failure

    }
}

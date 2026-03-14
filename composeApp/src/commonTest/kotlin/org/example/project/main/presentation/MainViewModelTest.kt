package org.example.project.main.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import org.example.project.MockData
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.UserRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fakeRunAsync: ControlledFakeRunAsync
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getPagedReposUseCase: FakeGetPagedReposUseCase
    private lateinit var mainUiMapper: PagedResult.Mapper

    @BeforeTest
    fun setUp() {
        mainUiMapper = MainUiMapper()
        savedStateHandle = SavedStateHandle()
        fakeRunAsync = ControlledFakeRunAsync()
        getPagedReposUseCase = FakeGetPagedReposUseCase()
        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun `empty query result`() { //+
        getPagedReposUseCase.mockPagedResult(mock = PagedResult.EmptyResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        mainViewModel.query(userQuery = QUERY_EXAMPLE)
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()
        assertEquals(MainUiState.EmptyResult, uiValue.value)
    }

    @Test
    fun `success query result`() {  //++
        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.query(QUERY_EXAMPLE)
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchResultUi, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

    }

    @Test
    fun `failure query result`() { //++
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText
        mainViewModel.query(QUERY_EXAMPLE)

        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()

        assertEquals(failureResult, uiValue.value)
    }

    @Test
    fun `retry search query`() { //++
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        mainViewModel.query(QUERY_EXAMPLE)

        fakeRunAsync.returnFlowResult()
        assertEquals(failureResult, uiValue.value)

        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        mainViewModel.retry()
        assertEquals(MainUiState.Loading, uiValue.value)
        fakeRunAsync.invokeUi()
        assertEquals(successSearchResultUi, uiValue.value)

        //process death
        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
        )

        val newUiState: StateFlow<MainUiState> = mainViewModel.mainUiState
        val newSearchText = mainViewModel.searchText.value

        assertEquals(successSearchResultUi, newUiState.value)
        assertEquals(QUERY_EXAMPLE, newSearchText)
    }


    @Test
    fun `success load user repositories`() { //++
        getPagedReposUseCase.mockPagedResult(mock = successUserRepoPagedResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoResultUi, uiValue.value)
        assertEquals("", searchText.value)
    }

    @Test
    fun `failure load user repositories then success`() {
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(failureResult, uiValue.value)
        assertEquals("", searchText.value)

        getPagedReposUseCase.mockPagedResult(mock = successUserRepoPagedResult)

        mainViewModel.retry()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoResultUi, uiValue.value)
        assertEquals("", searchText.value)

    }

    @Test
    fun `success user repo paging test`() {
        getPagedReposUseCase.mockPagedResult(mock = successUserRepoPagedResult)

        val uiState = mainViewModel.mainUiState
        val searchText = mainViewModel.searchText

        assertEquals(MainUiState.Loading, uiState.value)
        assertEquals("", searchText.value)

        mainViewModel.loadUserRepo()

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoResultUi, uiState.value)
        assertEquals("", searchText.value)

        getPagedReposUseCase.mockPagedResult(
            mock = PagedResult.Success(
                repos = MockData.firstAndSecondUserRepo,
                isLoadMore = true,
                isPagingException = false,
                page = 2,
            )
        )

        mainViewModel.loadMore(
            isLoadMore = true,
            currentRepoList = MockData.mockedUserRepositoriesUi,
            page = 1
        )

        fakeRunAsync.invokeUi()

        assertEquals(
            successUserRepoResultUi.copy(
                page = 2,
                result = MockData.firstAndSecondUserRepoUi,
                isLoadMore = true,
            ),
            uiState.value,
        )

        getPagedReposUseCase.mockPagedResult(
            mock = PagedResult.Success(
                repos = MockData.firstAndSecondUserRepo,
                isLoadMore = false,
                isPagingException = false,
                page = 3,
            )
        )

        mainViewModel.loadMore(
            isLoadMore = true,
            currentRepoList = MockData.firstAndSecondUserRepoUi,
            page = 2,
        )

        fakeRunAsync.invokeUi()

        assertEquals(
            successUserRepoResultUi.copy(
                page = 3,
                result = MockData.firstAndSecondUserRepoUi,
                isLoadMore = false,
                pagingUiState = PagingUiState.Empty,
            ),
            uiState.value,
        )
    }

    @Test
    fun `failure query paging test`() {
        val uiState = mainViewModel.mainUiState

        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        mainViewModel.query(QUERY_EXAMPLE)
        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchResultUi, uiState.value)


        getPagedReposUseCase.mockPagedResult(
            mock = successSearchPagedResult.copy(
                isPagingException = true,
                isLoadMore = true,

                )
        )

        mainViewModel.loadMore(
            isLoadMore = true,
            currentRepoList = MockData.mockedSearchRepositoriesUi,
            page = 1,
        )

        fakeRunAsync.invokeUi()

        assertEquals(
            successSearchResultUi.copy(
                pagingUiState = PagingUiState.Failure(message = "something went wrong"),
            ), uiState.value
        )
    }

    companion object {
        private const val QUERY_EXAMPLE = "query example"
    }

}

private val successSearchResultUi = MainUiState.Success(
    page = 1,
    isLoadMore = true,
    result = MockData.mockedSearchRepositoriesUi,
    pagingUiState = PagingUiState.Loading,
)

private val successUserRepoResultUi = MainUiState.Success(
    page = 1,
    isLoadMore = true,
    result = MockData.mockedUserRepositoriesUi,
    pagingUiState = PagingUiState.Loading,
)


private val failureResult = MainUiState.Failure(
    message = "something went wrong"
)

private val successSearchPagedResult =
    PagedResult.Success(
        isPagingException = false,
        isLoadMore = true,
        repos = MockData.mockedSearchResults,
        page = 1,
    )

private val successUserRepoPagedResult =
    PagedResult.Success(
        isPagingException = false,
        isLoadMore = true,
        repos = MockData.mockedRepositories,
        page = 1,
    )

private val failurePagedResult = PagedResult.Failure("something went wrong")


private class FakeGetPagedReposUseCase : GetPagedReposUseCase {

    private lateinit var mockedPageResult: PagedResult

    override suspend fun userRepo(
        currentRepoList: List<UserRepository>,
        page: Int
    ): PagedResult {
        return mockedPageResult
    }

    override suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int
    ): PagedResult {
        return mockedPageResult
    }

    fun mockPagedResult(mock: PagedResult) {
        mockedPageResult = mock
    }

}


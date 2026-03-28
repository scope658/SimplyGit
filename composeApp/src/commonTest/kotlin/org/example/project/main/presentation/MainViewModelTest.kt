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
    private lateinit var mainUiMapper: PagedResult.Mapper<MainUiState>

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

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        mainViewModel.query(userQuery = QUERY_EXAMPLE)
        assertEquals(initialState, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()
        assertEquals(
            initialState.copy(
                mainUiState = MainUiState.EmptyResult
            ), uiValue.value
        )
    }

    @Test
    fun `success query result`() {  //++
        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.query(QUERY_EXAMPLE)
        assertEquals(initialState, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchMainScreenState, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

    }

    @Test
    fun `failure query result`() { //++
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText
        mainViewModel.query(QUERY_EXAMPLE)

        assertEquals(initialState, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()

        assertEquals(
            initialState.copy(
                mainUiState = failureResult
            ), uiValue.value
        )
    }

    @Test
    fun `retry search query`() { //++
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        mainViewModel.query(QUERY_EXAMPLE)

        fakeRunAsync.returnFlowResult()
        assertEquals(
            initialState.copy(
                mainUiState = failureResult
            ), uiValue.value
        )

        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        mainViewModel.retry()
        assertEquals(initialState, uiValue.value)
        fakeRunAsync.invokeUi()
        assertEquals(successSearchMainScreenState, uiValue.value)

        //process death
        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
        )

        val newUiState: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val newSearchText = mainViewModel.searchText.value

        assertEquals(successSearchMainScreenState, newUiState.value)
        assertEquals(QUERY_EXAMPLE, newSearchText)
    }


    @Test
    fun `success load user repositories`() { //++
        getPagedReposUseCase.mockPagedResult(mock = successUserRepoPagedResult)

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoScreenState, uiValue.value)
        assertEquals("", searchText.value)
    }

    @Test
    fun `failure load user repositories then success`() {
        getPagedReposUseCase.mockPagedResult(mock = failurePagedResult)

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(
            initialState.copy(
                mainUiState = failureResult
            ), uiValue.value
        )
        assertEquals("", searchText.value)

        getPagedReposUseCase.mockPagedResult(mock = successUserRepoPagedResult)

        mainViewModel.retry()
        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoScreenState, uiValue.value)
        assertEquals("", searchText.value)

    }

    @Test
    fun `failure query paging test`() {
        val uiState = mainViewModel.mainUiState

        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult)

        mainViewModel.query(QUERY_EXAMPLE)
        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchMainScreenState, uiState.value)


        getPagedReposUseCase.mockPagedResult(
            mock = successSearchPagedResult.copy(
                isPagingException = true,
                isLoadMore = true,
            )
        )

        mainViewModel.loadMore(
            currentRepoList = MockData.mockedSearchRepositoriesUi,
            page = 1,
        )

        fakeRunAsync.invokeUi()

        getPagedReposUseCase.mockPagedResult(mock = successSearchPagedResult.copy(page = 2))
        assertEquals(
            expected = initialState.copy(
                mainUiState = successSearchResultUi.copy(
                    pagingUiState = PagingUiState.Failure(message = "something went wrong"),
                )
            ), uiState.value
        )
    }

    @Test
    fun refresh() {
        val uiState = mainViewModel.mainUiState
        getPagedReposUseCase.mockPagedResult(successUserRepoPagedResult)
        mainViewModel.refresh()

        assertEquals(initialState.copy(isRefreshing = true), uiState.value)
        fakeRunAsync.invokeUi()

        assertEquals(
            successUserRepoScreenState.copy(
                isRefreshing = false
            ),
            uiState.value
        )

    }

    companion object {
        private const val QUERY_EXAMPLE = "query example"
    }

}

private val successUserRepoResultUi = MainUiState.Success(
    page = 0,
    isLoadMore = false,
    result = MockData.mockedUserRepositoriesUi,
    pagingUiState = PagingUiState.Empty,
)

private val successSearchResultUi =
    MainUiState.Success(
        page = 1,
        isLoadMore = true,
        result = MockData.mockedSearchRepositoriesUi,
        pagingUiState = PagingUiState.Loading,
    )


private val successSearchMainScreenState = MainScreenState(
    isRefreshing = false,
    mainUiState = successSearchResultUi,

    )
private val successUserRepoScreenState = MainScreenState(
    isRefreshing = false,
    mainUiState = successUserRepoResultUi
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
        isLoadMore = false,
        repos = MockData.mockedRepositories,
        page = 0,
    )

private val failurePagedResult = PagedResult.Failure("something went wrong")

private val initialState = MainScreenState(
    isRefreshing = false,
    mainUiState = MainUiState.Loading,
)

private class FakeGetPagedReposUseCase : GetPagedReposUseCase {

    private lateinit var mockedPageResult: PagedResult
    private var refreshCalledTimes = 0

    fun checkRefreshCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, refreshCalledTimes)
    }

    override suspend fun allUserRepos(): PagedResult {
        return mockedPageResult
    }

    override suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int
    ): PagedResult {
        return mockedPageResult
    }

    override suspend fun refresh(): PagedResult {
        refreshCalledTimes++
        return mockedPageResult
    }

    fun mockPagedResult(mock: PagedResult) {
        mockedPageResult = mock
    }

}


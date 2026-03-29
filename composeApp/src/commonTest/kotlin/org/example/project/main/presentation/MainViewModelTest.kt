package org.example.project.main.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import org.example.project.MockData
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.PaginationResult
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.mappers.MainUiMapper
import org.example.project.main.presentation.mappers.PagingUiStateMapper
import org.example.project.main.presentation.mappers.UserRepoToUiMapper
import org.example.project.main.presentation.mappers.UserRepoUiToDomain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fakeRunAsync: ControlledFakeRunAsync
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getPagedReposUseCase: FakeGetPagedReposUseCase
    private lateinit var mainUiMapper: PagedResult.Mapper<MainUiState>
    private lateinit var userRepoUiToDomain: UserRepositoryUi.Mapper<UserRepository>

    @BeforeTest
    fun setUp() {
        val userRepoToUiMapper: UserRepository.Mapper<UserRepositoryUi> = UserRepoToUiMapper()
        val pagingUiStateMapper: PaginationResult.Mapper<PagingUiState> = PagingUiStateMapper()
        userRepoUiToDomain = UserRepoUiToDomain()

        mainUiMapper = MainUiMapper(
            userRepoToUiMapper = userRepoToUiMapper,
            pagingUiStateMapper = pagingUiStateMapper
        )

        savedStateHandle = SavedStateHandle()
        fakeRunAsync = ControlledFakeRunAsync()
        getPagedReposUseCase = FakeGetPagedReposUseCase()


    }

    @Test
    fun `empty query result`() {
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = PagedResult.Failure("fake"))
        getPagedReposUseCase.mockUserQueryPagedResult(mock = PagedResult.EmptyResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )


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
        getPagedReposUseCase.mockUserQueryPagedResult(mock = successSearchPagedResult)
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = PagedResult.EmptyResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

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
        getPagedReposUseCase.mockUserQueryPagedResult(mock = failurePagedResult)
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = PagedResult.EmptyResult)


        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

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
        getPagedReposUseCase.mockUserQueryPagedResult(mock = failurePagedResult)
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = PagedResult.EmptyResult)


        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        mainViewModel.query(QUERY_EXAMPLE)

        fakeRunAsync.returnFlowResult()
        assertEquals(
            initialState.copy(
                mainUiState = failureResult
            ), uiValue.value
        )

        getPagedReposUseCase.mockUserQueryPagedResult(mock = successSearchPagedResult)

        mainViewModel.retry()
        assertEquals(initialState, uiValue.value)

        fakeRunAsync.invokeUi()
        assertEquals(successSearchMainScreenState, uiValue.value)
    }


    @Test
    fun `success load user repositories`() { //++
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = successUserRepoPagedResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoScreenState, uiValue.value)
        assertEquals("", searchText.value)
    }

    @Test
    fun `failure load user repositories then success`() {
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = failurePagedResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

        val uiValue: StateFlow<MainScreenState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(
            initialState.copy(
                mainUiState = failureResult
            ), uiValue.value
        )
        assertEquals("", searchText.value)

        getPagedReposUseCase.mockAllUserReposPagedResult(mock = successUserRepoPagedResult)

        mainViewModel.retry()
        assertEquals(initialState, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoScreenState, uiValue.value)
        assertEquals("", searchText.value)
    }

    @Test
    fun `failure query paging test`() {
        getPagedReposUseCase.mockUserQueryPagedResult(mock = successSearchPagedResult)
        getPagedReposUseCase.mockAllUserReposPagedResult(mock = PagedResult.EmptyResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

        val uiState = mainViewModel.mainUiState
        fakeRunAsync.invokeUi()

        mainViewModel.query(QUERY_EXAMPLE)
        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchMainScreenState, uiState.value)


        getPagedReposUseCase.mockUserQueryPagedResult(
            mock = successSearchPagedResult.copy(
                paginationResult = PaginationResult.Failure("something went wrong")
            )
        )

        mainViewModel.loadMore(
            currentRepoList = MockData.mockedSearchRepositoriesUi,
            page = 1,
        )

        fakeRunAsync.invokeUi()

        getPagedReposUseCase.mockUserQueryPagedResult(mock = successSearchPagedResult.copy(page = 2))
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
        getPagedReposUseCase.mockAllUserReposPagedResult(successUserRepoPagedResult)

        mainViewModel = MainViewModel(
            getPagedReposUseCase = getPagedReposUseCase,
            mainUiMapper = mainUiMapper,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
            userRepoUiToDomain = userRepoUiToDomain,
        )

        val uiState = mainViewModel.mainUiState

        mainViewModel.refresh()

        assertEquals(initialState.copy(isRefreshing = true), uiState.value)
        fakeRunAsync.invokeUi()

        getPagedReposUseCase.checkRefreshCalled(1)
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

    result = MockData.mockedUserRepositoriesUi,
    pagingUiState = PagingUiState.Empty,
)

private val successSearchResultUi =
    MainUiState.Success(
        page = 1,
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
        paginationResult = PaginationResult.ReadyForNext,
        repos = MockData.mockedSearchResults,
        page = 1,
    )

private val successUserRepoPagedResult =
    PagedResult.Success(
        paginationResult = PaginationResult.ReachedBottom,
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
    private lateinit var searchByQueryPagedResult: PagedResult

    private var refreshCalledTimes = 0
    private var allUserReposCalledTimes = 0
    private var userQueryCalledTimes = 0

    override suspend fun allUserRepos(): PagedResult {
        allUserReposCalledTimes++
        return mockedPageResult
    }

    override suspend fun searchByQuery(
        currentRepoList: List<UserRepository>,
        userQuery: String,
        page: Int
    ): PagedResult {
        userQueryCalledTimes++
        return searchByQueryPagedResult
    }

    override suspend fun refresh(): PagedResult {
        refreshCalledTimes++
        return mockedPageResult
    }

    fun mockAllUserReposPagedResult(mock: PagedResult) {
        mockedPageResult = mock
    }

    fun mockUserQueryPagedResult(mock: PagedResult) {
        searchByQueryPagedResult = mock
    }

    fun checkRefreshCalled(expectedCalledTimes: Int) {
        assertEquals(expectedCalledTimes, refreshCalledTimes)
    }
}

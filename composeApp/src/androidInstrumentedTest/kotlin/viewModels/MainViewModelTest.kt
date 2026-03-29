package viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.ViewModelScenario
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.example.project.MockData
import org.example.project.core.presentation.RunAsync
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.PaginationResult
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.MainScreenState
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.MainViewModel
import org.example.project.main.presentation.PagingUiState
import org.example.project.main.presentation.UserRepositoryUi
import org.example.project.main.presentation.mappers.MainUiMapper
import org.example.project.main.presentation.mappers.PagingUiStateMapper
import org.example.project.main.presentation.mappers.UserRepoToUiMapper
import org.example.project.main.presentation.mappers.UserRepoUiToDomain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

abstract class AbstractViewModelTest {
    protected fun ViewModelScenario<*>.assertBeforeAndAfterProcessDeath(block: () -> Unit) {
        block.invoke()
        this.recreate()
        block.invoke()
    }
}

@RunWith(AndroidJUnit4::class)
class MainViewModelTest : AbstractViewModelTest() {

    private lateinit var fakeRunAsync: FakeRunAsync
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getPagedReposUseCase: FakeGetPagedReposUseCase
    private lateinit var mainUiMapper: PagedResult.Mapper<MainUiState>
    private lateinit var userRepoUiToDomain: UserRepositoryUi.Mapper<UserRepository>

    @Before
    fun setUp() {
        val userRepoToUiMapper: UserRepository.Mapper<UserRepositoryUi> = UserRepoToUiMapper()
        val pagingUiStateMapper: PaginationResult.Mapper<PagingUiState> = PagingUiStateMapper()
        userRepoUiToDomain = UserRepoUiToDomain()

        mainUiMapper = MainUiMapper(
            userRepoToUiMapper = userRepoToUiMapper,
            pagingUiStateMapper = pagingUiStateMapper
        )

        savedStateHandle = SavedStateHandle()
        fakeRunAsync = FakeRunAsync()
        getPagedReposUseCase = FakeGetPagedReposUseCase()
    }

    @Test
    fun loadUserRepoProcessDeath() = runBlocking {
        getPagedReposUseCase.mockAllUserReposPagedResult(failurePagedResult)
        viewModelScenario {
            MainViewModel(
                getPagedReposUseCase = getPagedReposUseCase,
                mainUiMapper = mainUiMapper,
                runAsync = fakeRunAsync,
                savedStateHandle = savedStateHandle,
                userRepoUiToDomain = userRepoUiToDomain,
            )
        }.use { scenario ->

            assertEquals(
                successUserRepoScreenState.copy(
                    mainUiState = MainUiState.Failure("something went wrong")
                ), scenario.viewModel.mainUiState.value
            )
            assertEquals("", scenario.viewModel.searchText.value)

            getPagedReposUseCase.checkAllUserRepoCalled(1)
            getPagedReposUseCase.mockAllUserReposPagedResult(successUserRepoPagedResult)


            scenario.recreate()

            assertEquals(successUserRepoScreenState, scenario.viewModel.mainUiState.value)
            getPagedReposUseCase.checkAllUserRepoCalled(2)
        }
    }


    @Test
    fun userQueryProcessDeath() {
        getPagedReposUseCase.mockAllUserReposPagedResult(PagedResult.EmptyResult)
        getPagedReposUseCase.mockUserQueryPagedResult(failurePagedResult)

        viewModelScenario {
            MainViewModel(
                getPagedReposUseCase = getPagedReposUseCase,
                mainUiMapper = mainUiMapper,
                runAsync = fakeRunAsync,
                savedStateHandle = createSavedStateHandle(),
                userRepoUiToDomain = userRepoUiToDomain,
            )
        }.use { scenario ->
            val viewModelUiState = scenario.viewModel.mainUiState
            val queryText = scenario.viewModel.searchText

            getPagedReposUseCase.checkAllUserRepoCalled(1)

            assertEquals("", queryText.value)

            scenario.viewModel.query("query")

            scenario.assertBeforeAndAfterProcessDeath {
                assertEquals("query", queryText.value)
                assertEquals(
                    MainScreenState(
                        isRefreshing = false,
                        MainUiState.Failure("something went wrong")
                    ),
                    viewModelUiState.value
                )
                getPagedReposUseCase.checkUserQueryCalled(1)
            }

            getPagedReposUseCase.mockUserQueryPagedResult(successSearchPagedResult)

            scenario.viewModel.retry()

            scenario.assertBeforeAndAfterProcessDeath {
                assertEquals("query", scenario.viewModel.searchText.value)
                assertEquals(successSearchMainScreenState, scenario.viewModel.mainUiState.value)
                getPagedReposUseCase.checkUserQueryCalled(2)
            }
        }
    }
}

private class FakeGetPagedReposUseCase : GetPagedReposUseCase {

    private lateinit var mockedPageResult: PagedResult
    private lateinit var searchByQueryPagedResult: PagedResult

    private var refreshCalledTimes = 0
    private var allUserReposCalledTimes = 0
    private var userQueryCalledTimes = 0

    fun checkUserQueryCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, userQueryCalledTimes)
    }

    fun checkAllUserRepoCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, allUserReposCalledTimes)
    }

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

class FakeRunAsync : RunAsync {

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: suspend (T) -> Unit,
    ) {
        runBlocking {
            ui.invoke(background.invoke())
        }
    }

    override fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: suspend (T) -> Unit,
    ) {
        runBlocking {
            val flowResult = flow.first()
            onEach.invoke(flowResult)
        }
    }
}

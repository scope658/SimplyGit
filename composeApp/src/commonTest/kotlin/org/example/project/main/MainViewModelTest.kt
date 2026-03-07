package org.example.project.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import org.example.project.MockData
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.MainUiState
import org.example.project.main.presentation.MainViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainRepository: FakeMainRepository
    private lateinit var fakeRunAsync: ControlledFakeRunAsync
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeTest
    fun setUp() {
        savedStateHandle = SavedStateHandle()
        mainRepository = FakeMainRepository()
        fakeRunAsync = ControlledFakeRunAsync()
        mainViewModel = MainViewModel(
            repository = mainRepository,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun `empty query result`() {
        mainRepository.mockResult(emptyList())
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
    fun `success query result`() {
        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.query(QUERY_EXAMPLE)
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()
        assertEquals(successSearchResult, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)
    }

    @Test
    fun `failure query result`() {
        mainRepository.mockFailure(failure = true)
        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText
        mainViewModel.query(QUERY_EXAMPLE)

        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals(QUERY_EXAMPLE, searchText.value)

        fakeRunAsync.returnFlowResult()

        assertEquals(failureResult, uiValue.value)
    }

    @Test
    fun `retry search query`() {
        mainRepository.mockFailure(true)
        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        mainViewModel.query(QUERY_EXAMPLE)

        fakeRunAsync.returnFlowResult()
        assertEquals(failureResult, uiValue.value)

        mainRepository.mockFailure(false)
        mainViewModel.retry()
        assertEquals(MainUiState.Loading, uiValue.value)
        fakeRunAsync.invokeUi()
        assertEquals(successSearchResult, uiValue.value)

        //process death
        val mainViewModel = MainViewModel(
            repository = mainRepository,
            runAsync = fakeRunAsync,
            savedStateHandle = savedStateHandle,
        )

        val newUiState: StateFlow<MainUiState> = mainViewModel.mainUiState
        val newSearchText = mainViewModel.searchText.value
        assertEquals(successSearchResult, newUiState.value)
        assertEquals(QUERY_EXAMPLE, newSearchText)
    }


    @Test
    fun `success load user repositories`() {
        mainRepository.mockResult(mockedResult = MockData.mockedRepositories)
        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)


        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoResult, uiValue.value)
        assertEquals("", searchText.value)
    }

    @Test
    fun `failure load user repositories then success`() {
        mainRepository.mockFailure(true)
        mainRepository.mockResult(MockData.mockedRepositories)
        val uiValue: StateFlow<MainUiState> = mainViewModel.mainUiState
        val searchText: StateFlow<String> = mainViewModel.searchText

        mainViewModel.loadUserRepo()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(failureResult, uiValue.value)
        assertEquals("", searchText.value)

        mainRepository.mockFailure(false)

        mainViewModel.retry()
        assertEquals(MainUiState.Loading, uiValue.value)
        assertEquals("", searchText.value)

        fakeRunAsync.invokeUi()

        assertEquals(successUserRepoResult, uiValue.value)
        assertEquals("", searchText.value)
    }

    companion object {
        private const val QUERY_EXAMPLE = "query example"
    }

}

private val successSearchResult = MainUiState.Success(
    result = MockData.mockedSearchRepositoriesUi
)

private val successUserRepoResult = MainUiState.Success(
    result = MockData.mockedUserRepositoriesUi
)

private val failureResult = MainUiState.Failure(
    message = "something went wrong"
)

private class FakeMainRepository : MainRepository {

    private var mockedResult = MockData.mockedSearchResults
    private var isFailure = false

    override suspend fun userRepo(): Result<List<UserRepository>> {

        if (isFailure) {
            return Result.failure<List<UserRepository>>(IllegalStateException("something went wrong"))
        } else {
            return Result.success(mockedResult)
        }
    }

    override suspend fun searchByQuery(userQuery: String): Result<List<UserRepository>> {
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

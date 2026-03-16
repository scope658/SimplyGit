package org.example.project.main.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import org.example.project.core.RunAsync
import org.example.project.main.domain.GetPagedReposUseCase
import org.example.project.main.domain.PagedResult
import org.example.project.main.domain.UserRepository

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(
    private val getPagedReposUseCase: GetPagedReposUseCase,
    private val mainUiMapper: PagedResult.Mapper,
    private val runAsync: RunAsync,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), MainActions {

    private var savedState: MainUiState by savedStateHandle.saved(
        key = MAIN_UI_STATE_KEY,
        init = { MainUiState.Loading })

    private val _mainUiState: MutableStateFlow<MainScreenState> = MutableStateFlow(
        MainScreenState(isRefreshing = false, mainUiState = savedState)
    )
    val mainUiState = _mainUiState.asStateFlow()

    private val _searchText =
        savedStateHandle.getMutableStateFlow(SEARCH_TEXT_KEY, "")
    val searchText = _searchText.asStateFlow()

    private val isCurrentlyFetching: MutableStateFlow<Boolean> =
        savedStateHandle.getMutableStateFlow(CURRENTLY_FETCHING_KEY, false)

    private val initSearch by lazy {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = _searchText
                .debounce(SEARCH_DEBOUNCE)
                .filter { it.isNotBlank() }
                .mapLatest {
                    changeValue(MainUiState.Loading)
                    getPagedReposUseCase.searchByQuery(
                        userQuery = it,
                        currentRepoList = emptyList(),
                        page = 1,
                    )
                },
            onEach = { result ->
                changeValue(result.map(mapper = mainUiMapper))
            }
        )
    }

    init {
        Napier.d(message = "init in main", tag = "dd25")
        loadUserRepo()
    }

    private fun loadUserRepo() {
        val mainUiState = _mainUiState.value
        if (searchText.value.isNotBlank() || mainUiState is MainUiState.Success || mainUiState is MainUiState.EmptyResult) return
        changeValue(MainUiState.Loading)
        launchPagedRequest { firstPage, currentEmptyList ->
            getPagedReposUseCase.userRepo(
                page = firstPage,
                currentRepoList = currentEmptyList.toDomain()
            )
        }
    }

    override fun query(userQuery: String) {
        _searchText.value = userQuery
        initSearch
    }

    override fun retry() {

        if (_searchText.value.isNotBlank()) {
            changeValue(MainUiState.Loading)
            launchPagedRequest { firstPage, currentEmptyList ->
                getPagedReposUseCase.searchByQuery(
                    page = firstPage,
                    currentRepoList = currentEmptyList.toDomain(),
                    userQuery = _searchText.value
                )
            }
        } else {
            loadUserRepo()
        }
    }

    override fun loadMore(
        currentRepoList: List<UserRepositoryUi>,
        page: Int
    ) {
        val currentSearchText = _searchText.value
        when {
            isCurrentlyFetching.value -> return
            currentSearchText.isNotBlank() -> launchPagedRequest(
                page = page,
                currentRepoList
            ) { currentPage, currentRepoList ->
                getPagedReposUseCase.searchByQuery(
                    currentRepoList = currentRepoList.toDomain(),
                    userQuery = currentSearchText,
                    page = currentPage
                )
            }
        }
    }

    override fun refresh() {
        _mainUiState.update {
            it.copy(
                isRefreshing = true,
            )
        }
        launchPagedRequest { _, _ ->
            getPagedReposUseCase.refresh()
        }
    }

    private fun launchPagedRequest(
        page: Int = 1,
        currentRepoList: List<UserRepositoryUi> = emptyList(),
        request: suspend (Int, List<UserRepositoryUi>) -> PagedResult,
    ) {
        isCurrentlyFetching.value = true
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                request.invoke(page, currentRepoList)
            },
            ui = { pagedResult ->
                isCurrentlyFetching.value = false
                _mainUiState.value = pagedResult.map(mapper = mainUiMapper)
            }
        )
    }

    companion object {
        private const val MAIN_UI_STATE_KEY = "MAIN_UI_STATE_KEY"
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        private const val SEARCH_DEBOUNCE = 350L
        private const val CURRENTLY_FETCHING_KEY = "CURRENTLY_FETCHING_KEY"
    }
}


fun List<UserRepositoryUi>.toDomain() = this.map {
    UserRepository(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}

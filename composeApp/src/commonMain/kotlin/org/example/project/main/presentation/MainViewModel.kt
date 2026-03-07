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
import org.example.project.main.domain.MainRepository
import org.example.project.main.domain.UserRepository

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(
    private val repository: MainRepository,
    private val runAsync: RunAsync,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val _mainUiState: MutableStateFlow<MainUiState> =
        savedStateHandle.getMutableStateFlow(MAIN_UI_STATE_KEY, MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    private val _searchText =
        savedStateHandle.getMutableStateFlow(SEARCH_TEXT_KEY, "")
    val searchText = _searchText.asStateFlow()

    private val initSearch by lazy {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = _searchText
                .debounce(SEARCH_DEBOUNCE)
                .filter { it.isNotBlank() }
                .mapLatest {
                    repository.searchByQuery(it)
                },
            onEach = { result ->
                handleResult(result)
            }
        )
    }

    fun loadUserRepo() {
        _mainUiState.value = MainUiState.Loading
        runAsync.runAsync(
            scope = viewModelScope,
            background = { repository.userRepo() },
            ui = { result ->
                handleResult(result)
            }
        )
    }

    fun query(userQuery: String) {
        _searchText.value = userQuery
        initSearch
    }

    fun retry() {
        if (_searchText.value.isNotBlank()) {
            _mainUiState.value = MainUiState.Loading
            runAsync.runAsync(
                scope = viewModelScope,
                background = { repository.searchByQuery(_searchText.value) },
                ui = { result ->
                    handleResult(result)
                }
            )
        } else {
            loadUserRepo()
        }
    }

    private fun handleResult(result: Result<List<UserRepository>>) {
        result
            .onSuccess { repositories ->
                _mainUiState.value = if (repositories.isEmpty()) {
                    MainUiState.EmptyResult
                } else {
                    MainUiState.Success(result = repositories.toUi())
                }
            }
            .onFailure {
                _mainUiState.value =
                    MainUiState.Failure(message = it.message ?: "something went wrong")
            }
    }

    companion object {
        private const val MAIN_UI_STATE_KEY = "MAIN_UI_STATE_KEY"
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        private const val SEARCH_DEBOUNCE = 350L
    }
}

fun List<UserRepository>.toUi() = this.map {
    UserRepositoryUi(
        id = it.id,
        userPhotoImageUrl = it.userPhotoImageUrl,
        userName = it.userName,
        repositoryName = it.repositoryName,
        programmingLanguage = it.programmingLanguage,
        stars = it.stars
    )
}



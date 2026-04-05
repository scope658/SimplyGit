package org.example.project.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.presentation.RunAsync
import org.example.project.details.domain.CombinedDetailsResult
import org.example.project.details.domain.RepoDetailsUseCase

class DetailsViewModel(
    private val detailsUiMapper: CombinedDetailsResult.Mapper<DetailsUiState>,
    private val repoDetailsUseCase: RepoDetailsUseCase,
    private val runAsync: RunAsync,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), DetailsActions {

    private val _detailsScreenState: MutableStateFlow<DetailsScreenState> = MutableStateFlow(
        DetailsScreenState(
            isRefreshing = false,
            detailsUiState = DetailsUiState.Loading
        )
    )
    val detailsScreenState = _detailsScreenState.asStateFlow()

    private val _detailsEvent: MutableSharedFlow<DetailsEvent> = MutableSharedFlow()
    val detailsEvent: SharedFlow<DetailsEvent> = _detailsEvent.asSharedFlow()

    init {
        loadDetails(
            repoDetails = { repoDetailsUseCase.repoDetails("", "") },
        )
    }

    override fun retry() {
        _detailsScreenState.update { currentScreenState ->
            currentScreenState.copy(
                detailsUiState = DetailsUiState.Loading,
            )
        }
        loadDetails(
            repoDetails = { repoDetailsUseCase.repoDetails("", "") },
        )
    }

    override fun refresh() {
        _detailsScreenState.update { currentScreenState ->
            currentScreenState.copy(
                isRefreshing = true,
            )
        }

        loadDetails(
            repoDetails = { repoDetailsUseCase.refreshRepoDetails("", "") },
        )
    }

    override fun onCode(repoOwner: String, repoName: String) {
        emitEvent(event = DetailsEvent.OnCode(repoOwner, repoName))
    }

    override fun onCreateIssues(repoOwner: String, repoName: String) {
        emitEvent(event = DetailsEvent.OnCreateIssues(repoOwner, repoName))
    }

    private fun emitEvent(event: DetailsEvent) {
        runAsync.runAsync(
            viewModelScope,
            background = {},
            ui = { _detailsEvent.emit(event) }
        )
    }

    private fun loadDetails(
        repoDetails: suspend () -> CombinedDetailsResult,
    ) {
        runAsync.runAsync(
            viewModelScope,
            background = {
                repoDetails.invoke()
            },
            ui = { combinedDetailsResult ->
                _detailsScreenState.update { currentState ->
                    currentState.copy(
                        isRefreshing = false,
                        detailsUiState = combinedDetailsResult.map(mapper = detailsUiMapper)
                    )
                }
            }
        )
    }
}

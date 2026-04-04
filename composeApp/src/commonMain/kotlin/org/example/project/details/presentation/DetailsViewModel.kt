package org.example.project.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.core.presentation.RunAsync
import org.example.project.details.domain.DetailsRepository
import org.example.project.details.domain.RepoDetails

class DetailsViewModel(
    private val detailsUiMapper: RepoDetails.Mapper<DetailsUiState>,
    private val detailsRepository: DetailsRepository,
    private val runAsync: RunAsync,
    private val manageResource: ManageResource,
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
            repoDetails = { detailsRepository.repoDetails("", "") },
            readme = { detailsRepository.readme("", "") }
        )
    }

    override fun retry() {
        _detailsScreenState.update { currentScreenState ->
            currentScreenState.copy(
                detailsUiState = DetailsUiState.Loading,
            )
        }
        loadDetails(
            repoDetails = { detailsRepository.repoDetails("", "") },
            readme = { detailsRepository.readme("", "") }
        )
    }

    override fun refresh() {
        _detailsScreenState.update { currentScreenState ->
            currentScreenState.copy(
                isRefreshing = true,
            )
        }

        loadDetails(
            repoDetails = { detailsRepository.refreshDetails("", "") },
            readme = { detailsRepository.refreshReadme("", "") }
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
        repoDetails: suspend () -> Result<RepoDetails>,
        readme: suspend () -> Result<String>
    ) {
        runAsync.runAsync(
            viewModelScope,
            background = {
                coroutineScope {
                    val detailsDeferred =
                        async { repoDetails.invoke() }
                    val readmeDeferred =
                        async { readme.invoke() }
                    detailsDeferred.await() to readmeDeferred.await()

                }
            },
            ui = { (repoDetails, readme) ->
                val readme = readme.fold(
                    onSuccess = { readme ->
                        readme
                    },
                    onFailure = {
                        val exception = it as? DomainException ?: ServiceUnavailableException
                        exception.exceptionString(manageResource)
                    }
                )
                repoDetails.onSuccess { repoDetails ->
                    _detailsScreenState.update { currentScreenState ->
                        currentScreenState.copy(
                            isRefreshing = false,
                            detailsUiState = repoDetails.mapSuccess(
                                mapper = detailsUiMapper,
                                readme
                            )
                        )
                    }
                }
                    .onFailure {
                        val exception = it as? DomainException ?: ServiceUnavailableException
                        val message = exception.exceptionString(manageResource)
                        _detailsScreenState.update { currentScreenState ->
                            currentScreenState.copy(
                                isRefreshing = false,
                                detailsUiState = DetailsUiState.Failure(message)
                            )
                        }
                    }
            }
        )
    }
}

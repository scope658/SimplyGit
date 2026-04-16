package org.example.project.repoFiles.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.example.project.Routes
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.core.presentation.RouteArgs
import org.example.project.core.presentation.RunAsync
import org.example.project.repoFiles.domain.RepoFiles
import org.example.project.repoFiles.domain.RepoFilesRepository


@Serializable
data class RepoFileUi(
    val name: String,
    val fileType: FileTypeUi,
    val path: String,
    val downloadUrl: String,
    val size: Long,
)

class RepoFilesViewModel(
    private val repoFilesRepository: RepoFilesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync,
    private val repoFilesArgs: RouteArgs.All,
    private val mapper: RepoFiles.Mapper<RepoFileUi>,
    private val manageResource: ManageResource,
) : ViewModel(), RepoFilesActions {


    private var savedState: RepoFilesUiState by savedStateHandle.saved(
        key = REPO_FILES_UI_STATE_KEY,
        init = { RepoFilesUiState.Loading }
    )
    private val repoOwner = repoFilesArgs.repoOwner()
    private val repoName = repoFilesArgs.repoName()
    private val path = repoFilesArgs.path()

    private val _repoFilesEvent: MutableSharedFlow<RepoFilesEvent> = MutableSharedFlow()
    val repoFilesEvent = _repoFilesEvent.asSharedFlow()


    private val _repoFilesUiState: MutableStateFlow<RepoFilesUiState> =
        MutableStateFlow(savedState)

    val repoFilesUiState: StateFlow<RepoFilesUiState> = _repoFilesUiState.asStateFlow()

    init {
        loadFiles()
        Napier.d(path, tag = "ss221")
    }

    private var isNavigating = false

    override fun onFileDetails(downloadUrl: String) {
        emitEvent(
            RepoFilesEvent.OnFileDetails(
                repoOwner = repoOwner,
                repoName = repoName,
                path = path,
            )
        )
    }

    override fun openDirectory(targetPath: String) {
        if (isNavigating) return // Если уже нажали, игнорируем
        isNavigating = true
        emitEvent(
            RepoFilesEvent.OpenDirectory(
                repoOwner = repoOwner,
                repoName = repoName,
                path = targetPath,
            )
        )
        viewModelScope.launch {
            delay(500)
            isNavigating = false
        }
    }

    override fun retry() {
        loadFiles()
    }

    private fun emitEvent(event: RepoFilesEvent) {
        runAsync.runAsync(
            viewModelScope,
            background = {},
            ui = {
                _repoFilesEvent.emit(event)
            },
        )
    }

    private fun loadFiles() {
        runAsync.runAsync(
            viewModelScope,
            { repoFilesRepository.repoFiles(repoOwner, repoName, path) },
            { result ->
                result.onSuccess { repoFiles ->
                    savedState = RepoFilesUiState.Success(
                        repoFiles = repoFiles.map { repoFiles ->
                            repoFiles.mapSuccess(mapper)
                        }
                    )
                    _repoFilesUiState.value = savedState

                }.onFailure {
                    val exception = it as? DomainException ?: ServiceUnavailableException
                    savedState =
                        RepoFilesUiState.Failure(message = exception.exceptionString(manageResource))
                }
            }
        )
    }

    companion object {
        private const val REPO_FILES_UI_STATE_KEY = "REPO_FILES_UI_STATE_KEY"
    }
}

class RepoFilesArgs(private val savedStateHandle: SavedStateHandle) : RouteArgs.All {
    private val repoFilesRoute = savedStateHandle.toRoute<Routes.RepoFiles>()

    override fun repoOwner(): String {
        return repoFilesRoute.repoOwner
    }

    override fun repoName(): String {
        return repoFilesRoute.repoName
    }

    override fun path(): String {
        return repoFilesRoute.path
    }
}

package org.example.project.createIssues.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.Routes
import org.example.project.core.presentation.RunAsync
import org.example.project.createIssues.domain.CreateIssueUseCase
import org.example.project.createIssues.domain.Issue
import org.example.project.createIssues.domain.IssueResult


class IssuesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createIssueUseCase: CreateIssueUseCase,
    private val runAsync: RunAsync,
    private val issuesUiStateMapper: IssueResult.Mapper<IssueScreenState>
) : ViewModel(), IssuesActions {

    private var savedState: IssueScreenState by savedStateHandle.saved(
        key = ISSUES_UI_STATE_KEY,
        init = { IssueScreenState.initial }
    )
    private val _issueScreenState: MutableStateFlow<IssueScreenState> = MutableStateFlow(savedState)
    val issuesScreenState = _issueScreenState.asStateFlow()

    private val route: Routes.CreateIssue =
        savedStateHandle.toRoute()


    override fun create() {
        val currentState = savedState
        val title = currentState.title
        val body = currentState.body
        savedState = savedState.copy(
            buttonState = ButtonState.Loading,
            isCreateButtonActive = false,
        )
        _issueScreenState.update {
            savedState
        }
        runAsync.runAsync(
            viewModelScope,
            background = {
                createIssueUseCase.createIssue(
                    repoOwner = route.repoOwner,
                    repoName = route.repoName,
                    issue = Issue(
                        title = title,
                        body = body,
                    )
                )
            },
            { result ->
                savedState = result.map(mapper = issuesUiStateMapper, current = savedState)
                _issueScreenState.value = savedState
            }
        )
    }

    override fun onTitleChanged(title: String) {
        if (savedState.buttonState == ButtonState.Loading) return
        val updated = savedState.copy(title = title)
        savedState = updated.copy(isCreateButtonActive = updated.isValid())
        _issueScreenState.value = savedState
    }

    override fun onBodyChanged(body: String) {
        if (savedState.buttonState == ButtonState.Loading) return
        val updated = savedState.copy(body = body)
        savedState = updated.copy(isCreateButtonActive = updated.isValid())
        _issueScreenState.value = savedState
    }

    companion object {
        private const val ISSUES_UI_STATE_KEY = "ISSUES_UI_STATE_KEY"
    }
}

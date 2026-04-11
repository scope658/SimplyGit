package org.example.project.createIssues.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.Routes
import org.example.project.core.presentation.RouteArgs
import org.example.project.core.presentation.RunAsync
import org.example.project.createIssues.domain.CreateIssueUseCase
import org.example.project.createIssues.domain.Issue
import org.example.project.createIssues.domain.IssueResult


class IssuesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createIssueUseCase: CreateIssueUseCase,
    private val runAsync: RunAsync,
    private val issuesUiStateMapper: IssueResult.Mapper<IssueScreenState>,
    private val issueArgs: RouteArgs,
) : ViewModel(), IssuesActions {

    private var savedState: IssueScreenState by savedStateHandle.saved(
        key = ISSUES_UI_STATE_KEY,
        init = { IssueScreenState.initial }
    )
    private val _issueScreenState: MutableStateFlow<IssueScreenState> = MutableStateFlow(savedState)
    val issuesScreenState = _issueScreenState.asStateFlow()

    private val repoOwner = issueArgs.repoOwner()
    private val repoName = issueArgs.repoName()

    override fun create() {
        val currentState = savedState
        val title = currentState.title
        val body = currentState.body
        savedState = savedState.copy(
            buttonState = ButtonState.Loading,
            isCreateButtonActive = false,
        )
        _issueScreenState.value = savedState
        runAsync.runAsync(
            viewModelScope,
            background = {
                createIssueUseCase.createIssue(
                    repoOwner = repoOwner,
                    repoName = repoName,
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
        updateField { copy(title = title) }
    }

    override fun onBodyChanged(body: String) {
        updateField { copy(body = body) }
    }

    override fun cancel() {
        updateField { copy(onBack = true) }
    }

    private fun updateField(update: IssueScreenState.() -> IssueScreenState) {
        val updated = savedState.update()
        savedState = updated.copy(isCreateButtonActive = updated.isValid())
        _issueScreenState.value = savedState
    }

    companion object {
        private const val ISSUES_UI_STATE_KEY = "ISSUES_UI_STATE_KEY"
    }


}

class IssuesArgs(private val savedStateHandle: SavedStateHandle) : RouteArgs {
    private val issueRoute = savedStateHandle.toRoute<Routes.CreateIssue>()
    override fun repoOwner(): String {
        return issueRoute.repoOwner
    }

    override fun repoName(): String {
        return issueRoute.repoName
    }
}

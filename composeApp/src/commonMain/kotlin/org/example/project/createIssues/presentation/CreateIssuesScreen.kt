package org.example.project.createIssues.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateIssuesScreen(
    createIssuesViewModel: IssuesViewModel = koinViewModel(),
    onSuccess: () -> Unit
) {
    val createIssuesScreenState by createIssuesViewModel.issuesScreenState.collectAsStateWithLifecycle()

    if (createIssuesScreenState.onBack) {
        onSuccess()
    }
    CreateIssueUi(createIssuesScreenState, createIssuesViewModel)
}

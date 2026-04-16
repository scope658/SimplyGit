package org.example.project.repoFiles.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.example.project.core.presentation.GeneralFailureScreen
import org.example.project.core.presentation.GeneralLoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepoFilesScreen(
    repoFilesViewModel: RepoFilesViewModel = koinViewModel(), onNextFiles: (
        repoOwner: String, repoName: String, path: String
    ) -> Unit
) {
    val uiState by repoFilesViewModel.repoFilesUiState.collectAsStateWithLifecycle()
    val event = repoFilesViewModel.repoFilesEvent

    LaunchedEffect(Unit) {
        event.collectLatest { repoFilesEvent ->
            when (repoFilesEvent) {
                is RepoFilesEvent.OnFileDetails -> Unit
                is RepoFilesEvent.OpenDirectory -> onNextFiles(
                    repoFilesEvent.repoOwner,
                    repoFilesEvent.repoName,
                    repoFilesEvent.path
                )
            }
        }
    }
    when (val state = uiState) {
        is RepoFilesUiState.Failure -> GeneralFailureScreen(
            state.message,
            { repoFilesViewModel.retry() }
        )

        RepoFilesUiState.Loading -> GeneralLoadingIndicator()
        is RepoFilesUiState.Success -> RepoFileSuccess(state, repoFilesViewModel)
    }
}

@Composable
private fun RepoFileSuccess(
    repoFileSuccess: RepoFilesUiState.Success,
    repoFilesActions: RepoFilesActions
) {
    Scaffold {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(items = repoFileSuccess.repoFiles, key = { it.path }) { repoFileUi ->
                FileFolderItem(
                    name = repoFileUi.name,
                    onClick = {
                        when (repoFileUi.fileType) {
                            FileTypeUi.Dir -> repoFilesActions.openDirectory(repoFileUi.path)
                            FileTypeUi.File -> Unit
                            FileTypeUi.SymLink -> Unit

                        }

                    }
                )
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RepoFilesPreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            RepoFileSuccess(
                RepoFilesUiState.Success(
                    repoFiles = repoFiles,
                ),
                repoFilesActions = object : RepoFilesActions {
                    override fun onFileDetails(downloadUrl: String) = Unit

                    override fun openDirectory(targetPath: String) = Unit

                    override fun retry() = Unit

                }
            )
        }
    }
}

@Composable
fun FileFolderItem(
    name: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick),
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Folder,
                contentDescription = null,
                tint = Color(0xFF7CB3F7), // Характерный голубой цвет папки
                modifier = Modifier.size(28.dp)
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

private val repoFiles = listOf(
    RepoFileUi(
        name = "dir",
        fileType = FileTypeUi.Dir,
        path = "1",
        downloadUrl = "",
        size = 25,
    ), RepoFileUi(
        name = "file",
        fileType = FileTypeUi.File,
        path = "2",
        downloadUrl = "",
        size = 25,
    ), RepoFileUi(
        name = "SymLink",
        fileType = FileTypeUi.SymLink,
        path = "3",
        downloadUrl = "",
        size = 25,
    ), RepoFileUi(
        name = "dir",
        fileType = FileTypeUi.Dir,
        path = "4",
        downloadUrl = "",
        size = 25,
    ), RepoFileUi(
        name = "file",
        fileType = FileTypeUi.File,
        path = "5",
        downloadUrl = "",
        size = 25,
    ), RepoFileUi(
        name = "SymLink",
        fileType = FileTypeUi.SymLink,
        path = "6",
        downloadUrl = "",
        size = 25,
    )
)

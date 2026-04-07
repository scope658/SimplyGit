package org.example.project.details.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CallMerge
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.add_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.add_fav_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.create_issues_item_test_tag
import ktshwnumbertwo.composeapp.generated.resources.create_pr_item_test_tag
import ktshwnumbertwo.composeapp.generated.resources.drop_down_menu_test_tag
import ktshwnumbertwo.composeapp.generated.resources.forks_icon_test_tag
import ktshwnumbertwo.composeapp.generated.resources.forks_title_test_tag
import ktshwnumbertwo.composeapp.generated.resources.github_icon
import ktshwnumbertwo.composeapp.generated.resources.issues_count_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_icon_test_tag
import ktshwnumbertwo.composeapp.generated.resources.issues_title_test_tag
import ktshwnumbertwo.composeapp.generated.resources.programming_language_test_tag
import ktshwnumbertwo.composeapp.generated.resources.repo_desc_test_tag
import ktshwnumbertwo.composeapp.generated.resources.repo_files_test_tag
import ktshwnumbertwo.composeapp.generated.resources.repo_name_test_tag
import ktshwnumbertwo.composeapp.generated.resources.repo_owner_test_tag
import ktshwnumbertwo.composeapp.generated.resources.repo_readme_test_tag
import org.example.project.details.presentation.DetailsUiState
import org.example.project.details.presentation.ReadmeUiState
import org.example.project.main.presentation.components.handleCorrectColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.indicatorSize

@Composable
fun DetailsSuccessScreen(detailsUiState: DetailsUiState.Success) = with(detailsUiState) {
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repoOwner,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.testTag(stringResource(Res.string.repo_owner_test_tag))
                )
                IconButton(
                    onClick = { /* toggle local favorite */ },
                    modifier = Modifier.testTag(stringResource(Res.string.add_fav_button_test_tag))
                ) {
                    Icon(Icons.Outlined.Favorite, contentDescription = null)
                }
                Box {
                    IconButton(
                        onClick = { isMenuExpanded = true },
                        modifier = Modifier.testTag(stringResource(Res.string.add_button_test_tag))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.testTag(stringResource(Res.string.drop_down_menu_test_tag))
                    ) {
                        DropdownMenuItem(
                            text = { Text("New Issue") },
                            leadingIcon = { Icon(Icons.Default.BugReport, null) },
                            onClick = {
                                isMenuExpanded = false
                                /* TODO: handle */
                            },
                            modifier = Modifier.testTag(stringResource(Res.string.create_issues_item_test_tag))
                        )
                        DropdownMenuItem(
                            text = { Text("New Pull Request") },
                            leadingIcon = { Icon(Icons.Default.CallMerge, null) },
                            onClick = {
                                isMenuExpanded = false
                                /* TODO: handle */
                            },
                            modifier = Modifier.testTag(stringResource(Res.string.create_pr_item_test_tag))
                        )
                    }
                }
            }
            Text(
                text = repoName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
                    .testTag(stringResource(Res.string.repo_name_test_tag))
            )

            if (repoDesc.isNotEmpty()) {
                Text(
                    text = repoDesc,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                        .testTag(stringResource(Res.string.repo_desc_test_tag))
                )
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(Res.drawable.github_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .testTag(stringResource(Res.string.forks_icon_test_tag))
                )
                Text(
                    text = " $forksCount forks",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag(stringResource(Res.string.forks_title_test_tag))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.size(indicatorSize)
                        .background(
                            color = handleCorrectColor(language = programmingLanguage),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = programmingLanguage,
                    modifier = Modifier.testTag(stringResource(Res.string.programming_language_test_tag))
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

        Column {
            MenuRow(
                icon = Icons.Default.Info,
                label = "Issues",
                value = issuesCount,
                iconBgColor = Color(0xFF4CAF50),
                iconTestTag = stringResource(Res.string.issues_icon_test_tag),
                labelTestTag = stringResource(Res.string.issues_title_test_tag),
                valueTestTag = stringResource(Res.string.issues_count_test_tag)
            )
            MenuRow(
                icon = Icons.Default.FileOpen,
                label = "Code",
                value = "",
                iconBgColor = Color.Black.copy(0.6f),
                labelTestTag = stringResource(Res.string.repo_files_test_tag),
                iconTestTag = "code_icon",
                valueTestTag = ""
            )
        }
        when (val state = readme) {
            is ReadmeUiState.Success -> Text(
                text = state.readme,
                modifier = Modifier.testTag(stringResource(Res.string.repo_readme_test_tag))
            )
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconBgColor: Color,
    iconTestTag: String,
    labelTestTag: String,
    valueTestTag: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(iconBgColor)
                .testTag(iconTestTag),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = label,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .testTag(labelTestTag),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.testTag(valueTestTag)
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailsSuccessPreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            DetailsSuccessScreen(
                detailsUiState = DetailsUiState.Success(
                    repoOwner = "repo owner",
                    repoName = "repo name",
                    repoDesc = "repo desc",
                    forksCount = "25",
                    issuesCount = "25",
                    programmingLanguage = "kotlin",
                    readme = ReadmeUiState.Success("readme")
                )
            )
        }
    }
}

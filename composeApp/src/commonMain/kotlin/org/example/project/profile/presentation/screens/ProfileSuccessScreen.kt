package org.example.project.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.logout_button_test_tag
import ktshwnumbertwo.composeapp.generated.resources.profile_logout_button
import ktshwnumbertwo.composeapp.generated.resources.profile_stats_followers
import ktshwnumbertwo.composeapp.generated.resources.profile_stats_repositories
import ktshwnumbertwo.composeapp.generated.resources.repo_count_test_tag
import ktshwnumbertwo.composeapp.generated.resources.subscribers_count_test_tag
import ktshwnumbertwo.composeapp.generated.resources.user_avatar_test_tag
import ktshwnumbertwo.composeapp.generated.resources.user_bio_test_tag
import ktshwnumbertwo.composeapp.generated.resources.user_name_test_tag
import org.example.project.profile.presentation.ProfileActions
import org.example.project.profile.presentation.ProfileUiState
import org.jetbrains.compose.resources.stringResource
import theme.avatarSizeHuge
import theme.iconSizeTiny
import theme.smallLineHeight
import theme.spacingL
import theme.spacingML
import theme.spacingXL
import theme.spacingXS
import theme.tinyAvatarBorder
import theme.tinyThicknessDivider

@Composable
fun ProfileSuccessScreen(profileUiState: ProfileUiState.Success, profileActions: ProfileActions) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(spacingL)
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = profileUiState.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(avatarSizeHuge)
                    .clip(CircleShape)
                    .border(tinyAvatarBorder, Color.LightGray, CircleShape)
                    .testTag(stringResource(Res.string.user_avatar_test_tag)),
                contentScale = ContentScale.Crop,

                )

            Spacer(modifier = Modifier.width(spacingL))

            Text(
                text = profileUiState.userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(stringResource(Res.string.user_name_test_tag))
            )
        }

        Spacer(modifier = Modifier.height(spacingML))

        Text(
            text = profileUiState.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            lineHeight = smallLineHeight,
            modifier = Modifier.testTag(stringResource(Res.string.user_bio_test_tag))
        )

        Spacer(modifier = Modifier.height(spacingL))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(iconSizeTiny),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(spacingXS))

            Text(
                text = profileUiState.subscribersCount,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag(stringResource(Res.string.subscribers_count_test_tag))
            )
            Spacer(modifier = Modifier.width(spacingXS))
            Text(
                text = stringResource(Res.string.profile_stats_followers),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.width(spacingL))

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(iconSizeTiny),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(spacingXS))

            Text(
                text = profileUiState.repoCount,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag(stringResource(Res.string.repo_count_test_tag))
            )
            Spacer(modifier = Modifier.width(spacingXS))
            Text(
                text = stringResource(Res.string.profile_stats_repositories),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = spacingXL),
            thickness = tinyThicknessDivider,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = profileActions::logout,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(Res.string.logout_button_test_tag)),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Red.copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = stringResource(Res.string.profile_logout_button),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SuccessPreview() {
    ProfileSuccessScreen(
        profileActions = previewProfileActions, profileUiState = ProfileUiState.Success(
            avatar = "fake",
            userName = "scope",
            bio = "fake bio",
            repoCount = "12",
            subscribersCount = "23",
        )
    )

}

private val previewProfileActions = object : ProfileActions {
    override fun logout() = Unit

    override fun retry() = Unit
    override fun refresh() = Unit

}

package org.example.project.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.compose_multiplatform
import ktshwnumbertwo.composeapp.generated.resources.programming_lang_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.repo_name_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.repo_star_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.star_icon_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.user_name_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.user_photo_tag_prefix
import ktshwnumbertwo.composeapp.generated.resources.user_repo_card_tag_prefix
import org.example.project.MockData
import org.example.project.main.presentation.UserRepositoryUi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.LanguageColors
import theme.avatarShape
import theme.avatarSizeMedium
import theme.fontSizeS
import theme.indicatorSize
import theme.spacingM
import theme.spacingS
import theme.spacingXS
import theme.spacingXXS


@Composable
fun RepositoryCard(userRepositoryUi: UserRepositoryUi) {

    val id = userRepositoryUi.id

    val cardTag = "${stringResource(Res.string.user_repo_card_tag_prefix)}$id"
    val photoTag = "${stringResource(Res.string.user_photo_tag_prefix)}$id"
    val nameTag = "${stringResource(Res.string.user_name_tag_prefix)}$id"
    val repoNameTag = "${stringResource(Res.string.repo_name_tag_prefix)}$id"
    val starIconTag = "${stringResource(Res.string.star_icon_tag_prefix)}$id"
    val starTextTag = "${stringResource(Res.string.repo_star_tag_prefix)}$id"
    val langTag = "${stringResource(Res.string.programming_lang_tag_prefix)}$id"

    Box(modifier = Modifier.testTag(cardTag).fillMaxWidth()) {
        Column(modifier = Modifier.padding(spacingM)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = userRepositoryUi.userPhotoImageUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.compose_multiplatform),
                    error = painterResource(Res.drawable.compose_multiplatform),
                    modifier = Modifier.size(avatarSizeMedium).testTag(photoTag)
                        .clip(shape = avatarShape)
                )
                Spacer(modifier = Modifier.width(spacingS))
                Text(text = userRepositoryUi.userName, modifier = Modifier.testTag(nameTag))
            }
            Spacer(modifier = Modifier.height(spacingXS))
            Text(
                text = userRepositoryUi.repositoryName,
                fontSize = fontSizeS,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag(repoNameTag)
            )
            Spacer(modifier = Modifier.height(spacingXS))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = null,
                    modifier = Modifier.testTag(starIconTag)
                )
                Spacer(modifier = Modifier.width(spacingXXS))
                Text(
                    userRepositoryUi.stars.toString(),
                    modifier = Modifier.testTag(starTextTag)
                )
                Spacer(modifier = Modifier.width(spacingM))
                Box(
                    modifier = Modifier.size(indicatorSize)
                        .background(
                            color = handleCorrectColor(language = userRepositoryUi.programmingLanguage),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(spacingXXS))
                Text(
                    userRepositoryUi.programmingLanguage,
                    modifier = Modifier.testTag(langTag)
                )
            }
        }
    }
}

fun handleCorrectColor(language: String): Color = when (language.trim().lowercase()) {
    "javascript" -> LanguageColors.JavaScript
    "python" -> LanguageColors.Python
    "java" -> LanguageColors.Java
    "typescript" -> LanguageColors.TypeScript
    "kotlin" -> LanguageColors.Kotlin
    "go" -> LanguageColors.Go
    "rust" -> LanguageColors.Rust
    "c++" -> LanguageColors.Cpp
    "c" -> LanguageColors.C
    "c#" -> LanguageColors.CSharp
    "php" -> LanguageColors.Php
    "swift" -> LanguageColors.Swift
    "ruby" -> LanguageColors.Ruby
    "html" -> LanguageColors.Html
    "css" -> LanguageColors.Css
    "shell", "bash" -> LanguageColors.Shell
    "dart" -> LanguageColors.Dart
    "objective-c" -> LanguageColors.ObjectiveC
    "r" -> LanguageColors.R
    "scala" -> LanguageColors.Scala
    else -> LanguageColors.Unknown
}

@Preview(showBackground = true)
@Composable
private fun RepositoryCardPreview() {
    RepositoryCard(MockData.mockedUserRepositoriesUi[2])
}

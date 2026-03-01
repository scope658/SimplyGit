package org.example.project.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.compose_multiplatform
import org.example.project.MockData
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainUi(listRepository: List<UserRepositoryUi>) {
    LazyColumn(modifier = Modifier.testTag("main_lazy_column")) {
        items(items = listRepository, key = { it.id }) { userRepositoryUi ->
            RepositoryCard(userRepositoryUi)
            HorizontalDivider()
        }
    }
}

@Composable
fun RepositoryCard(userRepositoryUi: UserRepositoryUi) {
    val id = userRepositoryUi.id
    Box(modifier = Modifier.testTag("user_repo_card_$id")) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = userRepositoryUi.userPhotoImageUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.compose_multiplatform),
                    error = painterResource(Res.drawable.compose_multiplatform),
                    modifier = Modifier.size(30.dp).testTag("user_photo_$id")
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(text = userRepositoryUi.userName, modifier = Modifier.testTag("user_name_$id"))
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = userRepositoryUi.repositoryName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("repo_name_$id")
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = null,
                    modifier = Modifier.testTag("star_icon_$id")
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    userRepositoryUi.stars.toString(),
                    modifier = Modifier.testTag("repo_star_$id")
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier.size(10.dp)
                        .background(color = Color(0xFFA97BFF), shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    userRepositoryUi.programmingLanguage,
                    modifier = Modifier.testTag("programming_lang_$id")
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun MainUiPreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MainUi(
                listRepository = MockData.mockedUserRepositoriesUi
            )
        }
    }
}
package org.example.project.main.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.empty_icon_test_tag
import ktshwnumbertwo.composeapp.generated.resources.empty_result_text_test_tag
import ktshwnumbertwo.composeapp.generated.resources.nothing_image
import ktshwnumbertwo.composeapp.generated.resources.nothing_to_see_here
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.fontSizeXXL

@Composable
fun MainEmptyResultScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.nothing_image),
            contentDescription = null,
            modifier = Modifier.testTag(stringResource(Res.string.empty_icon_test_tag))
        )
        Text(
            stringResource(Res.string.nothing_to_see_here),
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSizeXXL,
            modifier = Modifier.testTag(stringResource(Res.string.empty_result_text_test_tag))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainEmptyPreview() {
    MainEmptyResultScreen()
}

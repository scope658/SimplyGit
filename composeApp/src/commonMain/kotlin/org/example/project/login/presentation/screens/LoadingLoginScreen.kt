package org.example.project.login.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import theme.progressIndicatorLarge

@Composable
fun LoadingLoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(progressIndicatorLarge))
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoadingLoginScreenPreview() {
    LoadingLoginScreen()
}

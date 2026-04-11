package org.example.project.createIssues.presentation

import kotlinx.serialization.Serializable


@Serializable
sealed interface ButtonState {
    object Init : ButtonState
    object Loading : ButtonState
    data class Failure(val message: String) : ButtonState
}

@Serializable
data class IssueScreenState(
    val title: String,
    val titleSuppText: String,
    val body: String,
    val bodySuppText: String,
    val isCreateButtonActive: Boolean,
    val onBack: Boolean,
    val buttonState: ButtonState,
) {
    companion object {
        val initial = IssueScreenState(
            title = "",
            body = "",
            isCreateButtonActive = false,
            titleSuppText = "",
            bodySuppText = "",
            onBack = false,
            buttonState = ButtonState.Init
        )
    }

    fun isValid(): Boolean {
        if (buttonState is ButtonState.Loading) return false
        return title.isNotBlank() && body.isNotBlank()
    }
}//65536 body max // title max is 256

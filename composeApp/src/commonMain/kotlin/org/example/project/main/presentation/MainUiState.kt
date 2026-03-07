package org.example.project.main.presentation

import org.example.project.CommonParcelable
import org.example.project.CommonParcelize

interface MainUiState : CommonParcelable {

    @CommonParcelize
    data class Success(
        val result: List<UserRepositoryUi>
    ) : MainUiState

    @CommonParcelize
    object Loading : MainUiState

    @CommonParcelize
    object EmptyResult : MainUiState

    @CommonParcelize
    data class Failure(val message: String) : MainUiState
}

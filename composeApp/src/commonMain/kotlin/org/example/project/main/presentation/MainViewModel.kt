package org.example.project.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.core.RunAsync
import org.example.project.main.domain.ListRepository
import org.example.project.main.domain.toUi

class MainViewModel(private val repository: ListRepository, private val runAsync: RunAsync) :
    ViewModel() {
    private val _userRepositoriesUi: MutableStateFlow<List<UserRepositoryUi>> =
        MutableStateFlow(emptyList())
    val userRepositoriesUi: StateFlow<List<UserRepositoryUi>> = _userRepositoriesUi.asStateFlow()

    init {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                repository.getList()
            },
            {
                _userRepositoriesUi.value = it.toUi()
            }
        )
    }
}

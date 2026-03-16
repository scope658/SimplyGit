package org.example.project.main.presentation

interface MainActions {
    fun query(userQuery: String)
    fun retry()
    fun loadMore(currentRepoList: List<UserRepositoryUi>, page: Int)
    fun refresh()
}

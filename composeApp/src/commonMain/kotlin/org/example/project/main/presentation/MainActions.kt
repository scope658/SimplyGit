package org.example.project.main.presentation

interface MainActions {
    fun loadUserRepo()
    fun query(userQuery: String)
    fun retry()
    fun loadMore(isLoadMore: Boolean, currentRepoList: List<UserRepositoryUi>, page: Int)
}

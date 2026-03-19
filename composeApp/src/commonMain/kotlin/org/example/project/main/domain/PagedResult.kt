package org.example.project.main.domain

interface PagedResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun mapSuccess(
            page: Int,
            repos: List<UserRepository>,
            paginationResult: PaginationResult,
        ): T

        fun mapFailure(message: String): T
        fun emptyResult(): T
    }


    data class Success(
        val page: Int,
        val paginationResult: PaginationResult,
        val repos: List<UserRepository>,
    ) : PagedResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(page = page, repos = repos, paginationResult)
        }
    }

    data class Failure(
        val message: String,
    ) : PagedResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapFailure(message)
        }
    }

    object EmptyResult : PagedResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.emptyResult()
        }
    }
}

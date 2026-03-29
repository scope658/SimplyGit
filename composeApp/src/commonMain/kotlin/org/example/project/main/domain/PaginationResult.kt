package org.example.project.main.domain

interface PaginationResult {
    fun <T : Any> map(mapper: Mapper<T>): T
    interface Mapper<T> {
        fun mapReadyForNext(): T
        fun mapReachedBottom(): T
        fun mapFailure(message: String): T
    }

    object ReadyForNext : PaginationResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapReadyForNext()
        }
    }

    object ReachedBottom : PaginationResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapReachedBottom()
        }
    }

    data class Failure(
        val message: String
    ) : PaginationResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapFailure(message)
        }
    }
}

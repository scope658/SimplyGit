package org.example.project.details.domain

interface CombinedDetailsResult {
    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun mapSuccess(repoDetails: RepoDetails, readme: String): T
        fun mapFailure(message: String): T
    }

    data class Failure(
        val message: String,
    ) : CombinedDetailsResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapFailure(message)
        }
    }

    data class Success(
        val repoDetails: RepoDetails,
        val readme: String,
    ) : CombinedDetailsResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(repoDetails, readme)
        }
    }
}

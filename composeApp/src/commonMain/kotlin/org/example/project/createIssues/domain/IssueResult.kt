package org.example.project.createIssues.domain

interface IssueResult {
    fun <T : Any> map(mapper: Mapper<T>, current: T): T

    interface Mapper<T> {
        fun mapLongTitle(message: String, current: T): T
        fun mapLongBody(message: String, current: T): T
        fun mapFailure(message: String, current: T): T
        fun mapSuccess(current: T): T
        fun mapAllLong(titleMessage: String, bodyMessage: String, current: T): T
    }

    data class AllLong(val titleMessage: String, val bodyMessage: String) :
        IssueResult {
        override fun <T : Any> map(
            mapper: Mapper<T>,
            current: T
        ): T {
            return mapper.mapAllLong(titleMessage, bodyMessage, current)
        }

    }

    data class LongTitle(val message: String) : IssueResult {
        override fun <T : Any> map(
            mapper: Mapper<T>,
            current: T
        ): T {
            return mapper.mapLongTitle(message, current)
        }

    }

    data class LongBody(val message: String) : IssueResult {
        override fun <T : Any> map(
            mapper: Mapper<T>,
            current: T
        ): T {
            return mapper.mapLongBody(message, current)
        }

    }

    data class Failure(val message: String) : IssueResult {
        override fun <T : Any> map(
            mapper: Mapper<T>,
            current: T
        ): T {
            return mapper.mapFailure(message, current)
        }

    }

    object Success : IssueResult {
        override fun <T : Any> map(
            mapper: Mapper<T>,
            current: T
        ): T {
            return mapper.mapSuccess(current)
        }

    }
}


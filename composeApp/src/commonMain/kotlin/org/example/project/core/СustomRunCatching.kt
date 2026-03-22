package org.example.project.core

import kotlinx.coroutines.CancellationException

interface CustomRunCatching {

    suspend fun <R : Any> cath(block: suspend () -> R): Result<R>

    class Base(private val handleDomainError: HandleDomainError) : CustomRunCatching {
        override suspend fun <R : Any> cath(block: suspend () -> R): Result<R> {
            try {
                val data = block.invoke()
                return Result.success(data)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val handledException = handleDomainError.handle(e)
                return Result.failure(handledException)
            }
        }
    }
}

package org.example.project.core.data

import kotlinx.coroutines.CancellationException


class RunCatchingSuspend(private val handleDomainError: HandleDomainError) {
    suspend fun <R : Any> catch(block: suspend () -> R): Result<R> {
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


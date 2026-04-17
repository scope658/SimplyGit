package org.example.project.core.data

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException


class RunCatchingSuspend(private val handleDomainError: HandleDomainError) {
    suspend fun <R : Any> catch(block: suspend () -> R): Result<R> {
        try {
            val data = block.invoke()
            return Result.success(data)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Napier.e(throwable = e, tag = "RunCatching") { "Execution failed during suspend block" }
            val handledException = handleDomainError.handle(e)
            return Result.failure(handledException)
        }
    }
}


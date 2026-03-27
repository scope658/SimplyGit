package org.example.project.core

import kotlinx.coroutines.CancellationException

suspend fun <R : Any> runCatchingSuspend(block: suspend () -> R): Result<R> {
    try {
        val data = block.invoke()
        return Result.success(data)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        return Result.failure(IllegalStateException(e.message))
    }
}

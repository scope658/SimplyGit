package org.example.project.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RunAsync {
    fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    )

    fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: (T) -> Unit,
    )
}
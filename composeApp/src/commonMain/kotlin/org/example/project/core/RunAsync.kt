package org.example.project.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {
    fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: suspend (T) -> Unit,
    )

    fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: (T) -> Unit,
    )

    class Base : RunAsync {
        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            background: suspend () -> T,
            ui: suspend (T) -> Unit
        ) {
            scope.launch(Dispatchers.IO) {
                val data = background.invoke()
                withContext(Dispatchers.Main) {
                    ui.invoke(data)
                }
            }
        }

        override fun <T : Any> runFlow(
            scope: CoroutineScope,
            flow: Flow<T>,
            onEach: (T) -> Unit
        ) {
            flow.onEach(onEach).launchIn(scope)
        }

    }
}

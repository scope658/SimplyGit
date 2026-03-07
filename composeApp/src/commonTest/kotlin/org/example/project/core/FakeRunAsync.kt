package org.example.project.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync {

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: suspend (T) -> Unit,
    ) {
        runBlocking {
            ui.invoke(background.invoke())
        }
    }

    override fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: (T) -> Unit,
    ) {

        scope.launch(Dispatchers.Unconfined) {
            flow.collect {
                onEach.invoke(it)
            }
        }
    }
}

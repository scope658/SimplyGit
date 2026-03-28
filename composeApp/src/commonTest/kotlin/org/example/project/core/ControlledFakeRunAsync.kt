package org.example.project.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ControlledFakeRunAsync : RunAsync {

    private lateinit var backgroundResult: Any
    private lateinit var cachedUiAction: suspend (Any) -> Unit

    private lateinit var flowResult: Any
    private lateinit var cachedOnEach: (Any) -> Unit

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: suspend (T) -> Unit
    ) {
        runBlocking {
            backgroundResult = background.invoke()
            cachedUiAction = ui as suspend (Any) -> Unit
        }
    }

    override fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: (T) -> Unit
    ) {
        runBlocking {
            flowResult = flow.first()
            cachedOnEach = onEach as (Any) -> Unit
        }
    }

    fun invokeUi() = runBlocking {
        cachedUiAction.invoke(backgroundResult)
    }

    fun returnFlowResult() {
        cachedOnEach.invoke(flowResult)
    }
}

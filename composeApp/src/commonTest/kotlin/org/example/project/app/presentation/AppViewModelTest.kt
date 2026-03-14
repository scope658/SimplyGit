package org.example.project.app.presentation

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.example.project.app.domain.AppRepository
import org.example.project.core.FakeRunAsync
import kotlin.test.Test
import kotlin.test.assertEquals

class AppViewModelTest {


    @Test
    fun `user is onboarded and logged in`() = runBlocking {
        val appRepository = FakeAppRepository(isTokenSaved = true, isOnboarded = true)
        val appViewModel = AppViewModel(
            //code will be in init block
            runAsync = FakeRunAsync(),
            appRepository = appRepository,
        )
        val eventSharedFlow: SharedFlow<AppEvent> = appViewModel.appEvent
        val emitedEvent = withTimeout(TIMEOUT_TIME) { eventSharedFlow.first() }

        assertEquals(expected = AppEvent.OnMain, actual = emitedEvent)
    }

    @Test
    fun `user is already onboarded but not logged in`() = runBlocking {
        val appRepository = FakeAppRepository(isTokenSaved = false, isOnboarded = true)
        val appViewModel = AppViewModel(
            runAsync = FakeRunAsync(),
            appRepository = appRepository,
        )
        val eventSharedFlow: SharedFlow<AppEvent> = appViewModel.appEvent
        val emitedEvent = withTimeout(TIMEOUT_TIME) { eventSharedFlow.first() }

        assertEquals(expected = AppEvent.OnLoginIn, actual = emitedEvent)
    }

    @Test
    fun `user is not onboarded and logged in`() = runBlocking {
        val appRepository = FakeAppRepository(isTokenSaved = false, isOnboarded = false)
        val appViewModel = AppViewModel(
            runAsync = FakeRunAsync(),
            appRepository = appRepository,
        )
        val eventSharedFlow: SharedFlow<AppEvent> = appViewModel.appEvent
        val emitedEvent = withTimeout(TIMEOUT_TIME) { eventSharedFlow.first() }

        assertEquals(expected = AppEvent.OnOnboarding, actual = emitedEvent)
    }

    companion object {
        private const val TIMEOUT_TIME = 200L
    }
}

private class FakeAppRepository(
    private val isTokenSaved: Boolean,
    private val isOnboarded: Boolean
) :
    AppRepository {

    override suspend fun isTokenSaved(): Boolean {
        return isTokenSaved
    }

    override suspend fun isUserOnboarded(): Boolean {
        return this.isOnboarded
    }

}

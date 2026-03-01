package org.example.project

import org.example.project.core.FakeRunAsync
import org.example.project.main.domain.ListRepository
import org.example.project.main.domain.UserRepository
import org.example.project.main.presentation.MainViewModel
import org.example.project.main.presentation.UserRepositoryUi
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    @Test
    fun `init test`() {
        val fakeListRepository = FakeListRepository()
        val mainViewModel = MainViewModel(
            repository = fakeListRepository,
            runAsync = FakeRunAsync(),
        )
        val uiValue: List<UserRepositoryUi> = mainViewModel.userRepositoriesUi.value
        assertEquals(MockData.mockedUserRepositoriesUi, uiValue)
    }
}


private class FakeListRepository : ListRepository {

    private val mockedValues = MockData.mockedRepositories

    override suspend fun getList(): List<UserRepository> {
        return mockedValues
    }
}

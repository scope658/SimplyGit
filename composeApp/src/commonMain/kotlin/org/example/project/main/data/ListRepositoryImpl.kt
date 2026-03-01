package org.example.project.main.data

import org.example.project.MockData
import org.example.project.main.domain.ListRepository
import org.example.project.main.domain.UserRepository

class ListRepositoryImpl : ListRepository {
    override suspend fun getList(): List<UserRepository> {
        return MockData.mockedRepositories
    }
}
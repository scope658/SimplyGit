package org.example.project.main.domain

interface MainRepository {

    suspend fun userRepo(page: Int): Result<List<UserRepository>>

    suspend fun searchByQuery(userQuery: String, page: Int): Result<List<UserRepository>>
}

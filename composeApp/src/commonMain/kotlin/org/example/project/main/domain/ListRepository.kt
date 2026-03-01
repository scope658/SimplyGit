package org.example.project.main.domain

interface ListRepository {

    suspend fun getList(): List<UserRepository>

}
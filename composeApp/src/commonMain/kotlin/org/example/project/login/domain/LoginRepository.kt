package org.example.project.login.domain

interface LoginRepository {
    fun login(login: String, password: String): Result<Unit>
}
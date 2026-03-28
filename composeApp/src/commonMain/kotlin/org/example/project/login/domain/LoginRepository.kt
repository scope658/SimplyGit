package org.example.project.login.domain

interface LoginRepository {
     suspend fun userToken(): Result<String>
     suspend fun saveUserToken(token: String)
}

package org.example.project.login.data

import org.example.project.login.domain.LoginRepository

class LoginRepositoryImpl : LoginRepository {

    override fun login(login: String, password: String): Result<Unit> {
        return if (login == VALID_LOGIN && password == VALID_PASSWORD)
            Result.success(Unit)
        else
            Result.failure(IllegalStateException("mock exception from login repository"))
    }

    companion object {
        private const val VALID_LOGIN = "admin"
        private const val VALID_PASSWORD = "admin1234"

    }
}
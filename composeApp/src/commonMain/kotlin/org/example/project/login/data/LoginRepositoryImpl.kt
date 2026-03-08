package org.example.project.login.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.project.AuthWrapper
import org.example.project.TokenStorage
import org.example.project.core.customRunCatching
import org.example.project.login.domain.LoginRepository

class LoginRepositoryImpl(private val authWrapper: AuthWrapper) : LoginRepository {


    override suspend fun userToken(): Result<String> {
        return customRunCatching {
            authWrapper.userToken()
        }
    }

    override suspend fun saveUserToken(token: String) {
        withContext(Dispatchers.IO) {
            TokenStorage.token = token
        }
    }
}

package org.example.project.login.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.project.AuthWrapper
import org.example.project.TokenStorage
import org.example.project.login.domain.LoginRepository

class LoginRepositoryImpl(private val authWrapper: AuthWrapper) : LoginRepository {


    override suspend fun userToken(): Result<String> {
        try {
            val token = authWrapper.userToken()
            return Result.success(token)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(IllegalStateException(e.message))
        }
    }

    override suspend fun saveUserToken(token: String) {
        withContext(Dispatchers.IO) {
            TokenStorage.token = token
        }
    }
}

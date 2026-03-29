package org.example.project.app.data

import org.example.project.app.domain.AppRepository
import org.example.project.core.data.cache.DataStoreManager

class AppRepositoryImpl(private val dataStoreManager: DataStoreManager.Read) : AppRepository {

    override suspend fun isTokenSaved(): Boolean {
        val token = dataStoreManager.userToken()
        return token.isNotEmpty()
    }

    override suspend fun isUserOnboarded(): Boolean {
        return dataStoreManager.isOnboarded()
    }
}

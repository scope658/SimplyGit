package org.example.project.app.domain

interface AppRepository {
    suspend fun isTokenSaved(): Boolean
    suspend fun isUserOnboarded(): Boolean
}

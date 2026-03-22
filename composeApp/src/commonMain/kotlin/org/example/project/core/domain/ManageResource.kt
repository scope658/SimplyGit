package org.example.project.core.domain

interface ManageResource {
    suspend fun serviceUnavailable(): String
    suspend fun noConnection(): String
}

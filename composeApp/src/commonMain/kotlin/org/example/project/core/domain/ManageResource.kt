package org.example.project.core.domain

interface ManageResource {

    suspend fun serviceUnavailable(): String
    suspend fun noConnection(): String
    suspend fun readmeNotFound(): String

    suspend fun issuesDisabled(): String
    suspend fun serverValidation(): String
}

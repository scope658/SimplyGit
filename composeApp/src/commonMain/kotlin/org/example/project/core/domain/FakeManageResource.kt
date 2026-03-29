package org.example.project.core.domain

class FakeManageResource : ManageResource {
    override suspend fun serviceUnavailable(): String {
        return "service unavailable"
    }

    override suspend fun noConnection(): String {
        return "no connection"
    }
}

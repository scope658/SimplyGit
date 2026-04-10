package org.example.project.core.domain

class FakeManageResource : ManageResource {
    override suspend fun serviceUnavailable(): String {
        return "service unavailable"
    }

    override suspend fun noConnection(): String {
        return "no connection"
    }

    override suspend fun readmeNotFound(): String {
        return "readme not found"
    }

    override suspend fun issuesDisabled(): String {
        return "fake issues disabled"
    }

    override suspend fun serverValidation(): String {
        return "service validation"
    }
}

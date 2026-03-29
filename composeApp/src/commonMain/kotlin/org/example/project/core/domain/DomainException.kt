package org.example.project.core.domain


abstract class DomainException : Exception() {
    abstract suspend fun exceptionString(manageResource: ManageResource): String
}

object NoConnectionException : DomainException() {
    override suspend fun exceptionString(manageResource: ManageResource): String {
        return manageResource.noConnection()
    }
}

object ServiceUnavailableException : DomainException() {
    override suspend fun exceptionString(manageResource: ManageResource): String {
        return manageResource.serviceUnavailable()
    }
}




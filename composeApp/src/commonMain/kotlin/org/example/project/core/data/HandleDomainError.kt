package org.example.project.core.data

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import org.example.project.PlatformNetworkException
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.NoConnectionException
import org.example.project.core.domain.ServiceUnavailableException


interface HandleDomainError {
    fun handle(e: Exception): Exception
    class Base : HandleDomainError {
        override fun handle(e: Exception): Exception {
            return when (e) {
                is DomainException -> e
                is PlatformNetworkException,
                is SocketTimeoutException,
                is HttpRequestTimeoutException,
                    -> NoConnectionException

                else -> ServiceUnavailableException
            }
        }
    }
}

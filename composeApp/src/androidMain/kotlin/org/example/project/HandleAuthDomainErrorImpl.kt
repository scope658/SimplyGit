package org.example.project

import net.openid.appauth.AuthorizationException
import org.example.project.core.data.HandleDomainError
import org.example.project.core.domain.NoConnectionException
import org.example.project.core.domain.ServiceUnavailableException
import org.example.project.login.domain.ActionCancelledException

class HandleAuthDomainErrorImpl(private val generalErrorHandle: HandleDomainError) :
    HandleDomainError {
    override fun handle(e: Exception): Exception {
        return when (e) {
            is AuthorizationException -> {
                when {
                    e == AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW ||
                            e == AuthorizationException.AuthorizationRequestErrors.ACCESS_DENIED ->
                        ActionCancelledException

                    e == AuthorizationException.GeneralErrors.NETWORK_ERROR ->
                        NoConnectionException

                    else -> ServiceUnavailableException
                }
            }

            else -> generalErrorHandle.handle(e)
        }
    }
}

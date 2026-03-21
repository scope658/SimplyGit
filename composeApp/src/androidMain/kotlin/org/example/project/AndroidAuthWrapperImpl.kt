package org.example.project

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.ResponseTypeValues
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AndroidAuthWrapperImpl(context: Context) : AuthWrapper {

    private val authService = AuthorizationService(context)

    override suspend fun userToken(): String {

        val serviceConfig = AuthorizationServiceConfiguration(
            AUTHORIZATION_ENDPOINT.toUri(),
            TOKEN_ENDPOINT.toUri()
        )

        val request = AuthorizationRequest.Builder(
            serviceConfig, CLIENT_ID, ResponseTypeValues.CODE, REDIRECT_URI.toUri()
        ).setScopes(listOf(REPO, USER)).build()


        val authIntent = authService.getAuthorizationRequestIntent(request)


        val resultIntent = AuthCoordinator.requestAuth(authIntent)
            ?: throw Exception(USER_CANCELLED)

        val response = AuthorizationResponse.fromIntent(resultIntent)
            ?: throw Exception(AUTH_FAILED)

        return exchangeCodeForToken(response)

    }


    private suspend fun exchangeCodeForToken(response: AuthorizationResponse): String =
        suspendCancellableCoroutine { continuation ->

            val tokenRequest = response.createTokenExchangeRequest()

            authService.performTokenRequest(
                tokenRequest,
                ClientSecretPost(BuildConfig.CLIENT_SECRET)
            ) { tokenResponse, tokenException ->
                if (tokenResponse?.accessToken != null) {

                    continuation.resume(tokenResponse.accessToken!!)
                } else {
                    val errorMsg = tokenException?.errorDescription ?: "token exchange exception"
                    continuation.resumeWithException(Exception(errorMsg))
                }
            }

            continuation.invokeOnCancellation {
                authService.dispose()
            }
        }

    companion object {
        private const val REPO = "repo"
        private const val USER = "USER"
        private const val AUTH_FAILED = "Auth failed"
        private const val USER_CANCELLED = "User cancelled"
        private const val AUTHORIZATION_ENDPOINT = "https://github.com/login/oauth/authorize"
        private const val TOKEN_ENDPOINT = "https://github.com/login/oauth/access_token"
        private const val CLIENT_ID = "Ov23ligplx8h0iayKRbf"
        private const val REDIRECT_URI = "simplygit://callback"
    }
}

object AuthCoordinator {
    val authIntents = Channel<Intent>(Channel.CONFLATED)
    val authResults = Channel<Intent?>(Channel.CONFLATED)

    suspend fun requestAuth(intent: Intent): Intent? {
        authIntents.send(intent)
        return authResults.receive()
    }

    fun emitResult(intent: Intent?) {
        authResults.trySend(intent)
    }
}

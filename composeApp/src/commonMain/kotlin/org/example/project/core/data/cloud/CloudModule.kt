package org.example.project.core.data.cloud

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.core.data.cache.DataStoreManager
import org.koin.dsl.module


val cloudModule = module {
    single {
        val customHttClient = HttpClient {
            defaultRequest {
                url("https://api.github.com/")
                header("User-Agent", "SimplyGit-App")
                header("Accept", "application/vnd.github+json")

            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message, tag = "HTTP_DEBUG")
                    }
                }
            }
        }
        customHttClient.plugin(HttpSend).intercept { request ->
            val dataStore: DataStoreManager.ReadToken = get()
            val token = dataStore.userToken()

            if (token.isNotEmpty()) {
                request.header("Authorization", "Bearer $token")
            }
            execute(request)
        }

        customHttClient
    }
}

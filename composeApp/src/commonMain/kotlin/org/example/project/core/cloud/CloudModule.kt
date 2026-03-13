package org.example.project.core.cloud

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val cloudModule = module {
    single {
        HttpClient {
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
        }
    }
}

package org.example.project

class IosAuthWrapperImpl : AuthWrapper {
    override suspend fun userToken(): String {
        //TODO ADD IOS AUTH
        return "mock"
    }
}

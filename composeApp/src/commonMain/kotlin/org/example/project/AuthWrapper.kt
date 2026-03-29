package org.example.project

interface AuthWrapper {
    suspend fun userToken(): String
}

class FakeAuthWrapper : AuthWrapper {

    private var isFailure: Boolean = false
    private lateinit var exception: Exception
    override suspend fun userToken(): String {
        if (isFailure) {
            throw exception
        }
        return "fakeToken"

    }

    fun setException(isFailure: Boolean, expectedException: Exception = IllegalStateException()) {
        this.exception = expectedException
        this.isFailure = isFailure
    }
}



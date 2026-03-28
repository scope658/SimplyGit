package org.example.project

interface AuthWrapper {
    suspend fun userToken(): String
}

class FakeAuthWrapper : AuthWrapper {

    private var exception: Exception? = null

    override suspend fun userToken(): String {
        exception?.let {
            throw it
        }
        return "fakeToken"

    }

    fun setException(expectedException: Exception?) {
        exception = expectedException
    }
}



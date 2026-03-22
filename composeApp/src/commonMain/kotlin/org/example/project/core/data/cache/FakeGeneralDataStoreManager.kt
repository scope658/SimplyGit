package org.example.project.core.data.cache


class FakeGeneralDataStoreManager :
    DataStoreManager.All {

    private var token: String? = null
    private var onboardingFlag = false

    fun mockToken(token: String?) {
        this.token = token
    }

    fun mockOnboardedFlag(flag: Boolean) {
        this.onboardingFlag = flag
    }

    override suspend fun userToken(): String {
        return token ?: ""
    }

    override suspend fun isOnboarded(): Boolean {
        return onboardingFlag
    }

    override suspend fun saveUserToken(token: String) = Unit

    override suspend fun finishOnboarding() = Unit

}

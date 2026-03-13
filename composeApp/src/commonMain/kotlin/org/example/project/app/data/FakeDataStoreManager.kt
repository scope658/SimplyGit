import org.example.project.core.cache.DataStoreManager


class FakeDataStoreManager :
    DataStoreManager.Read {

    private var token: String? = null
    private var onboardingFlag = false

    fun mockToken(token: String?) {
        this.token = token
    }

    fun mockOnboardedFlag(flag: Boolean) {
        this.onboardingFlag = flag
    }

    override suspend fun userToken(): String? {
        return token
    }

    override suspend fun isOnboarded(): Boolean {
        return onboardingFlag
    }

}

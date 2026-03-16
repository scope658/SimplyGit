package org.example.project.profile.data

import kotlinx.coroutines.CancellationException
import org.example.project.core.cache.DataStoreManager
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileDao
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val githubApi: ProfileGithubApi,
    private val dataStoreManager: DataStoreManager.TokenOperations,
) : ProfileRepository {
    override suspend fun refreshUserProfile(): Result<Profile> {
        try {
            val userToken = dataStoreManager.userToken()
            val profileData = githubApi.userProfile(userToken ?: "")
            profileDao.saveUserProfile(profileCache = profileData.toCache())
            return Result.success(profileData.toDomain())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val profileCache = profileDao.readUserProfile()
            return if (profileCache != null) {
                Result.success(profileCache.toDomain())
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun logout() {
        dataStoreManager.saveUserToken("")
        profileDao.clearAll()
    }

    override suspend fun loadUserProfile(): Result<Profile> {
        val profileCache = profileDao.readUserProfile()
        if (profileCache != null) {
            return Result.success(profileCache.toDomain())
        } else {
            return refreshUserProfile()
        }
    }
}

fun ProfileCache.toDomain() = Profile(
    avatar = this.avatar,
    userName = this.userName,
    bio = this.bio,
    repoCount = this.repoCount,
    subscribersCount = this.subscribersCount
)

fun ProfileData.toDomain() = Profile(
    avatar = this.avatar,
    userName = this.userName,
    bio = this.bio,
    repoCount = this.repoCount,
    subscribersCount = this.subscribersCount
)

fun ProfileData.toCache() = ProfileCache(
    userId = this.userId,
    userName = this.userName,
    avatar = this.avatar,
    bio = this.bio,
    repoCount = this.repoCount,
    subscribersCount = this.subscribersCount
)

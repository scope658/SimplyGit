package org.example.project.profile.data

import kotlinx.coroutines.CancellationException
import org.example.project.core.data.HandleDomainError
import org.example.project.core.data.cache.DataStoreManager
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileDao
import org.example.project.profile.data.cloud.ProfileGithubApi
import org.example.project.profile.domain.Profile
import org.example.project.profile.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val githubApi: ProfileGithubApi,
    private val dataStoreManager: DataStoreManager.SaveToken,
    private val userReposDao: UserRepoDao.ClearAll,
    private val profileDataToDomain: ProfileData.Mapper<Profile>,
    private val profileDataToCache: ProfileData.Mapper<ProfileCache>,
    private val profileCacheToDomain: ProfileCache.Mapper<Profile>,
    private val handleDomainError: HandleDomainError,
) : ProfileRepository {
    override suspend fun refreshUserProfile(): Result<Profile> {
        try {
            val profileData = githubApi.userProfile()
            profileDao.saveUserProfile(profileCache = profileData.map(profileDataToCache))
            return Result.success(profileData.map(profileDataToDomain))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val profileCache = profileDao.readUserProfile()
            return if (profileCache != null) {
                Result.success(profileCache.map(mapper = profileCacheToDomain))
            } else {
                val handledException = handleDomainError.handle(e)
                Result.failure(handledException)
            }
        }
    }

    override suspend fun logout() {
        dataStoreManager.saveUserToken("")
        profileDao.clearAll()
        userReposDao.clearAll()
    }

    override suspend fun loadUserProfile(): Result<Profile> {
        val profileCache = profileDao.readUserProfile()
        if (profileCache != null) {
            return Result.success(profileCache.map(mapper = profileCacheToDomain))
        } else {
            return refreshUserProfile()
        }
    }
}

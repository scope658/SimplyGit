package org.example.project.profile.data.mappers

import org.example.project.profile.data.ProfileData
import org.example.project.profile.data.cache.ProfileCache

class ProfileDataToCache : ProfileData.Mapper<ProfileCache> {
    override fun map(profileData: ProfileData): ProfileCache = with(profileData) {
        return ProfileCache(
            userId = userId,
            userName = userName,
            avatar = avatar,
            bio = bio,
            repoCount = repoCount,
            subscribersCount = subscribersCount,
        )
    }
}

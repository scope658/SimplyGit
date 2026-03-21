package org.example.project.profile.data.cache

import org.example.project.profile.domain.Profile

class ProfileCacheToDomain : ProfileCache.Mapper<Profile> {
    override fun map(profileCache: ProfileCache): Profile = with(profileCache) {
        return Profile(
            avatar = avatar,
            userName = userName,
            bio = bio,
            repoCount = repoCount,
            subscribersCount = subscribersCount,
        )
    }
}

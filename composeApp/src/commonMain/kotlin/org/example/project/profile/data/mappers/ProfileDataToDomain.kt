package org.example.project.profile.data.mappers

import org.example.project.profile.data.ProfileData
import org.example.project.profile.domain.Profile

class ProfileDataToDomain : ProfileData.Mapper<Profile> {
    override fun map(profileData: ProfileData): Profile = with(profileData) {
        return Profile(
            avatar = avatar,
            userName = userName,
            bio = bio,
            repoCount = repoCount,
            subscribersCount = subscribersCount
        )
    }
}

package org.example.project.profile.presentation

import org.example.project.profile.domain.Profile

class ProfileUiMapper : Profile.Mapper<ProfileUiState> {
    override fun mapSuccess(
        profile: Profile
    ): ProfileUiState = with(profile) {
        return ProfileUiState.Success(
            avatar,
            userName,
            bio,
            repoCount.toString(),
            subscribersCount.toString()
        )
    }
}

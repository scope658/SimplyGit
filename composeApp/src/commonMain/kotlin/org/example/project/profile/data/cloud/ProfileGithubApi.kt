package org.example.project.profile.data.cloud

import org.example.project.profile.data.ProfileData

interface ProfileGithubApi {
    suspend fun userProfile(userToken: String): ProfileData
}

package org.example.project.profile.domain

data class Profile(
    val avatar: String,
    val userName: String,
    val bio: String,
    val repoCount: Int,
    val subscribersCount: Int,
) {
    interface Mapper<T> {
        fun mapSuccess(
            profile: Profile
        ): T
    }

    fun <T : Any> mapSuccess(mapper: Mapper<T>): T {
        return mapper.mapSuccess(profile = this)
    }
}

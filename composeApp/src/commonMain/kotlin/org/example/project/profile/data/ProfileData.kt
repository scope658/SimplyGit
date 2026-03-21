package org.example.project.profile.data

data class ProfileData(
    val userId: Long,
    val avatar: String,
    val userName: String,
    val bio: String,
    val repoCount: Int,
    val subscribersCount: Int,
) {
    interface Mapper<T> {
        fun map(profileData: ProfileData): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

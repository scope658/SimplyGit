package org.example.project.profile.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profile_table")
data class ProfileCache(
    @PrimaryKey(autoGenerate = false)
    val userId: Long,
    val userName: String,
    val avatar: String,
    val bio: String,
    val repoCount: Int,
    val subscribersCount: Int,
) {
    interface Mapper<T> {
        fun map(profileCache: ProfileCache): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }

}

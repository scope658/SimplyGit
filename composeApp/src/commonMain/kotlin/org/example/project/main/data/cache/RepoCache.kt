package org.example.project.main.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("user_repos_table")
data class RepoCache(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val userPhotoUrl: String,
    val userName: String,
    val repoName: String,
    val programmingLanguage: String,
    val stars: Int,
) {
    interface Mapper<T> {
        fun map(
            id: Long,
            userPhotoUrl: String,
            userName: String,
            repoName: String,
            programmingLanguage: String,
            stars: Int
        ): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(id, userPhotoUrl, userName, repoName, programmingLanguage, stars)
    }
}

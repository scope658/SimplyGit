package org.example.project.main.data.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface UserRepoDao {

    interface ClearAll {
        @Query("DELETE FROM user_repos_table")
        suspend fun clearAll()
    }

    @Query("SELECT * FROM user_repos_table")
    suspend fun readUserRepos(): List<RepoCache>

    @Upsert
    suspend fun addUserRepos(userRepos: List<RepoCache>)

    @Dao
    interface All : UserRepoDao, ClearAll
}

package org.example.project.details.data.cache.details

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DetailsDao {

    @Query("SELECT * FROM details_table WHERE repo_owner = :repoOwner AND repo_name = :repoName LIMIT 1")
    suspend fun details(repoOwner: String, repoName: String): DetailsCache?

    @Upsert
    suspend fun insert(repoCache: DetailsCache)

}

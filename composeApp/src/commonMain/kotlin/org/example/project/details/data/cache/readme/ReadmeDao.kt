package org.example.project.details.data.cache.readme

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReadmeDao {

    @Upsert
    suspend fun insert(readmeCache: ReadmeCache)


    @Query("SELECT * FROM readme_table WHERE repo_owner = :repoOwner AND repo_name = :repoName LIMIT 1")
    suspend fun readme(repoOwner: String, repoName: String): ReadmeCache?


}

package org.example.project.profile.data.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface ProfileDao {

    @Upsert
    suspend fun saveUserProfile(profileCache: ProfileCache)

    @Query("SELECT * FROM  profile_table LIMIT 1")
    suspend fun readUserProfile(): ProfileCache?

    @Query("DELETE FROM profile_table")
    suspend fun clearAll()
}



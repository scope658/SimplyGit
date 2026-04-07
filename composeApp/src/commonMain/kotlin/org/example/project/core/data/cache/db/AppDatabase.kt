package org.example.project.core.data.cache.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.example.project.details.data.cache.details.DetailsCache
import org.example.project.details.data.cache.details.DetailsDao
import org.example.project.details.data.cache.readme.ReadmeCache
import org.example.project.details.data.cache.readme.ReadmeDao
import org.example.project.main.data.cache.RepoCache
import org.example.project.main.data.cache.UserRepoDao
import org.example.project.profile.data.cache.ProfileCache
import org.example.project.profile.data.cache.ProfileDao


@Database(
    entities = [ProfileCache::class, RepoCache::class, DetailsCache::class, ReadmeCache::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun userRepoDao(): UserRepoDao.All
    abstract fun detailsDao(): DetailsDao
    abstract fun readmeDao(): ReadmeDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

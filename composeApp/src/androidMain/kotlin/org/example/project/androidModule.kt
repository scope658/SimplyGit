package org.example.project

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.core.cache.CommonDataStore
import org.example.project.core.cache.db.AppDatabase
import org.koin.dsl.module

val androidModule = module {
    single<AuthWrapper> { AndroidAuthWrapperImpl(context = get()) }
    single<DataStore<Preferences>> { provideDataStore(get()) }
    single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder(get()) }
}

fun provideDataStore(ctx: Context) = CommonDataStore.createDataStore(
    storePath = { fileName ->
        ctx.filesDir.resolve(fileName).absolutePath
    }
)

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

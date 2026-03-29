package org.example.project.core.data.cache

import org.example.project.core.data.cache.db.AppDatabase
import org.example.project.core.data.cache.db.getRoomDatabase
import org.koin.dsl.module


val cacheModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
}

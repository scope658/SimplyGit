package org.example.project.core.cache

import org.example.project.core.cache.db.AppDatabase
import org.example.project.core.cache.db.getRoomDatabase
import org.koin.dsl.module


val cacheModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
}

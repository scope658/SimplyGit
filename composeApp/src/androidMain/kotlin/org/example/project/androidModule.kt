package org.example.project

import android.content.Context
import org.example.project.core.cache.CommonDataStore
import org.example.project.core.cache.DataStoreManager
import org.koin.dsl.module

val androidModule = module {
    single<AuthWrapper> { AndroidAuthWrapperImpl(context = get()) }
    single<DataStoreManager.Read> { DataStoreManager.Base(provideDataStore(get())) }
}

fun provideDataStore(ctx: Context) = CommonDataStore.createDataStore(
    storePath = { fileName ->
        ctx.filesDir.resolve(fileName).absolutePath
    }
)

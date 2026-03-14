package org.example.project

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.project.core.cache.CommonDataStore
import org.koin.dsl.module

val androidModule = module {
    single<AuthWrapper> { AndroidAuthWrapperImpl(context = get()) }
    single<DataStore<Preferences>> { provideDataStore(get()) }
}

fun provideDataStore(ctx: Context) = CommonDataStore.createDataStore(
    storePath = { fileName ->
        ctx.filesDir.resolve(fileName).absolutePath
    }
)

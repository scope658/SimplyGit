package org.example.project.core.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

object CommonDataStore {
    fun createDataStore(
        fileName: String = DATA_STORE_FILE_NAME,
        storePath: (String) -> String,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { storePath.invoke(fileName).toPath() }
        )

    const val DATA_STORE_FILE_NAME = "app.preferences_pb"

}

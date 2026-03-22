package org.example.project

import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.core.data.cache.CommonDataStore
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask


@OptIn(ExperimentalForeignApi::class)
fun provideDataStore() = CommonDataStore.createDataStore(
    storePath = { fileName ->
        val documentDir: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(documentDir).path + "/${fileName}"
    }
)

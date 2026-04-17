package org.example.project

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

class CrashReportingAntilog : Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val crashlytics = FirebaseCrashlytics.getInstance()

        crashlytics.log("${priority.name}: [${tag ?: "NoTag"}] $message")


        if (throwable != null) {
            crashlytics.recordException(throwable)
        } else if (priority == LogLevel.ERROR) {
            crashlytics.recordException(Exception(message ?: "Unknown Error"))
        }
    }

}

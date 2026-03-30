package org.example.project

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import org.example.project.app.di.appModule
import org.example.project.core.data.cache.cacheModule
import org.example.project.core.data.cloud.cloudModule
import org.example.project.core.di.coreModule
import org.example.project.login.di.loginModule
import org.example.project.main.di.mainModule
import org.example.project.onboarding.di.onboardingModule
import org.example.project.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        } else {
            Napier.base(CrashReportingAntilog())
        }
        startKoin {
            androidContext(this@MyApp)
            modules(
                onboardingModule,
                loginModule,
                mainModule,
                androidModule,
                cloudModule,
                appModule,
                cacheModule,
                profileModule,
                coreModule,
            )
        }
    }
}

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

package org.example.project

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.example.project.app.di.appModule
import org.example.project.core.data.cache.cacheModule
import org.example.project.core.data.cloud.cloudModule
import org.example.project.core.di.coreModule
import org.example.project.createIssues.di.createIssuesModule
import org.example.project.details.di.detailsModule
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
                detailsModule,
                createIssuesModule,
            )
        }
    }
}


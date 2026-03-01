package org.example.project

import android.app.Application
import org.example.project.login.di.loginModule
import org.example.project.onboarding.di.onboardingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(onboardingModule, loginModule)
        }
    }
}
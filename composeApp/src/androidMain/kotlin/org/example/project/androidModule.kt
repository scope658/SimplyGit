package org.example.project

import org.koin.dsl.module

val androidModule = module {
    single<AuthWrapper> { AndroidAuthWrapperImpl(get()) }
}

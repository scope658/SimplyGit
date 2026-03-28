package org.example.project.core.di


import org.example.project.core.data.HandleDomainError
import org.example.project.core.data.RunCatchingSuspend
import org.example.project.core.domain.ManageResource
import org.example.project.core.presentation.ManageResourceImpl
import org.example.project.core.presentation.RunAsync
import org.koin.dsl.module

val coreModule = module {
    single<HandleDomainError> { HandleDomainError.Base() }
    single<RunCatchingSuspend> {
        RunCatchingSuspend(
            handleDomainError = get()
        )
    }
    single<ManageResource> { ManageResourceImpl() }
    single<RunAsync> { RunAsync.Base() }
}

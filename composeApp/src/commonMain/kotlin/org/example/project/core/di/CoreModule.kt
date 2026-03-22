package org.example.project.core.di

import org.example.project.core.CustomRunCatching
import org.example.project.core.HandleDomainError
import org.example.project.core.domain.ManageResource
import org.example.project.core.presentation.ManageResourceImpl
import org.koin.dsl.module

val coreModule = module {
    factory<HandleDomainError> { HandleDomainError.Base() }
    factory<CustomRunCatching> {
        CustomRunCatching.Base(
            handleDomainError = get()
        )
    }
    factory<ManageResource> { ManageResourceImpl() }
}

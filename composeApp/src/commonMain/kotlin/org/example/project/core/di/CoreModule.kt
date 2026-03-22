package org.example.project.core.di

import org.example.project.core.data.CustomRunCatching
import org.example.project.core.data.HandleDomainError
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

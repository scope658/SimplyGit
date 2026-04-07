package org.example.project.core.presentation

import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.error_readme_not_found
import ktshwnumbertwo.composeapp.generated.resources.noConnection
import ktshwnumbertwo.composeapp.generated.resources.serviceUnavailable
import org.example.project.core.domain.ManageResource
import org.jetbrains.compose.resources.getString

class ManageResourceImpl : ManageResource {
    override suspend fun serviceUnavailable(): String {
        return getString(Res.string.serviceUnavailable)
    }

    override suspend fun noConnection(): String {
        return getString(Res.string.noConnection)
    }

    override suspend fun readmeNotFound(): String {
        return getString(Res.string.error_readme_not_found)
    }
}

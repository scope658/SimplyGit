package org.example.project.core.presentation

import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.error_issue_creation_disabled
import ktshwnumbertwo.composeapp.generated.resources.error_readme_not_found
import ktshwnumbertwo.composeapp.generated.resources.error_server_validation
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

    override suspend fun issuesDisabled(): String {
        return getString(Res.string.error_issue_creation_disabled)
    }

    override suspend fun serverValidation(): String {
        return getString(Res.string.error_server_validation)
    }
}

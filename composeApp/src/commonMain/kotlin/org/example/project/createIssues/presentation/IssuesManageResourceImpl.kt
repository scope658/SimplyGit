package org.example.project.createIssues.presentation

import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.error_body_too_long
import ktshwnumbertwo.composeapp.generated.resources.error_title_too_long
import org.example.project.createIssues.domain.IssuesManageResource
import org.jetbrains.compose.resources.getString

class IssuesManageResourceImpl : IssuesManageResource {
    override suspend fun longBody(currentBodyLength: Int): String {
        return getString(Res.string.error_body_too_long, currentBodyLength)
    }

    override suspend fun longTitle(currentLongTitleLength: Int): String {
        return getString(Res.string.error_title_too_long, currentLongTitleLength)
    }
}

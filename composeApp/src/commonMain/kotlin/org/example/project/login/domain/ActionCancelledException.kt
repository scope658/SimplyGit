package org.example.project.login.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource

object ActionCancelledException : DomainException() {
    override suspend fun exceptionString(manageResource: ManageResource): String {
        return ""
    }
}

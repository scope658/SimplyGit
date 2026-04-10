package org.example.project.createIssues.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource

object IssuesDisabledException : DomainException() {
    override suspend fun exceptionString(manageResource: ManageResource): String {
        return manageResource.issuesDisabled()
    }
}

object ServerValidationException : DomainException() {
    override suspend fun exceptionString(manageResource: ManageResource): String {
        return manageResource.serverValidation()
    }
}

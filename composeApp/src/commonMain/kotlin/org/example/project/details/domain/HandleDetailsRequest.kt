package org.example.project.details.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException

class HandleDetailsRequest(private val manageResource: ManageResource) {
    suspend fun handle(
        readme: suspend () -> Result<String>,
        repoDetails: suspend () -> Result<RepoDetails>
    ): CombinedDetailsResult {
        val currentReadme = readme.invoke().fold(
            onSuccess = { readme ->
                readme
            }, onFailure = {
                val exception = it as? DomainException ?: ServiceUnavailableException
                val message = exception.exceptionString(manageResource = manageResource)
                message
            }
        )
        return repoDetails.invoke().fold(onSuccess = { repoDetails ->
            CombinedDetailsResult.Success(repoDetails, currentReadme)
        }, onFailure = {
            val exception = it as? DomainException ?: ServiceUnavailableException
            val message = exception.exceptionString(manageResource = manageResource)
            CombinedDetailsResult.Failure(message)
        }
        )
    }
}


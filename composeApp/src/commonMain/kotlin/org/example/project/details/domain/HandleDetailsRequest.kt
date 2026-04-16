package org.example.project.details.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException

class HandleDetailsRequest(private val manageResource: ManageResource) {
    suspend fun handle(
        readme: suspend () -> Result<String>,
        repoDetails: suspend () -> Result<RepoDetails>
    ): CombinedDetailsResult = coroutineScope {
        val readmeDeferred = async {
            readme().fold(
                onSuccess = { it },
                onFailure = {
                    val exception = it as? DomainException ?: ServiceUnavailableException
                    exception.exceptionString(manageResource = manageResource)
                }
            )
        }

        val detailsDeferred = async {
            repoDetails()
        }

        val readmeText = readmeDeferred.await()
        val detailsResult = detailsDeferred.await()

        detailsResult.fold(
            onSuccess = { details ->
                CombinedDetailsResult.Success(details, readmeText)
            },
            onFailure = {
                val exception = it as? DomainException ?: ServiceUnavailableException
                val message = exception.exceptionString(manageResource = manageResource)
                CombinedDetailsResult.Failure(message)
            }
        )
    }
}


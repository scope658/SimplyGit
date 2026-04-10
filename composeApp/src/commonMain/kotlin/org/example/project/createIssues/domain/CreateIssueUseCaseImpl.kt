package org.example.project.createIssues.domain

import org.example.project.core.domain.DomainException
import org.example.project.core.domain.ManageResource
import org.example.project.core.domain.ServiceUnavailableException

class CreateIssueUseCaseImpl(
    private val manageResource: ManageResource,
    private val issueValidator: IssueValidator,
    private val issueRepository: IssueRepository,
) :
    CreateIssueUseCase {
    override suspend fun createIssue(
        repoOwner: String,
        repoName: String,
        issue: Issue
    ): IssueResult {
        issueValidator.validate(issue)?.let { issueResult ->
            return issueResult
        }
        return issueRepository.createIssue(repoOwner, repoName, issue).fold(
            onSuccess = {
                IssueResult.Success
            }, onFailure = {
                val exception = it as? DomainException ?: ServiceUnavailableException
                val message = exception.exceptionString(manageResource)
                IssueResult.Failure(message)
            }
        )
    }
}

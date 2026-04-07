package org.example.project.details.domain

import io.github.aakira.napier.Napier

class RepoDetailsUseCaseImpl(
    private val handleDetailsRequest: HandleDetailsRequest,
    private val detailsRepository: DetailsRepository
) : RepoDetailsUseCase {
    override suspend fun repoDetails(
        repoOwner: String,
        repoName: String
    ): CombinedDetailsResult {
        return handleDetailsRequest.handle(readme = {
            Napier.d("repo details pinged", tag = "ss221")
            detailsRepository.readme(repoOwner, repoName)
        }, repoDetails = {
            detailsRepository.repoDetails(repoOwner, repoName)
        }
        )
    }

    override suspend fun refreshRepoDetails(
        repoOwner: String,
        repoName: String
    ): CombinedDetailsResult {
        return handleDetailsRequest.handle(readme = {
            detailsRepository.refreshReadme(repoOwner, repoName)
        }, repoDetails = {
            detailsRepository.refreshDetails(repoOwner, repoName)
        }
        )
    }
}

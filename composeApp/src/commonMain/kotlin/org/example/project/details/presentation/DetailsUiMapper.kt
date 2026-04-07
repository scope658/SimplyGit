package org.example.project.details.presentation

import io.github.aakira.napier.Napier
import org.example.project.details.domain.CombinedDetailsResult
import org.example.project.details.domain.RepoDetails

class DetailsUiMapper : CombinedDetailsResult.Mapper<DetailsUiState> {
    override fun mapSuccess(
        repoDetails: RepoDetails,
        readme: String
    ): DetailsUiState = with(repoDetails) {
        Napier.d(repoDetails.toString(), tag = "ss22")
        return DetailsUiState.Success(
            repoOwner = repoOwner,
            repoName = repoName,
            repoDesc = repoDesc,
            forksCount = forksCount.toString(),
            issuesCount = issuesCount.toString(),
            programmingLanguage = programmingLanguage,
            readme = ReadmeUiState.Success(readme),
        )
    }

    override fun mapFailure(message: String): DetailsUiState {
        return DetailsUiState.Failure(message)
    }
}

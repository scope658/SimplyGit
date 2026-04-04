package org.example.project.details.presentation

import org.example.project.details.domain.RepoDetails

class DetailsUiMapper : RepoDetails.Mapper<DetailsUiState> {
    override fun map(repoDetails: RepoDetails, readme: String): DetailsUiState = with(repoDetails) {
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
}

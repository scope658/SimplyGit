package org.example.project.details.data

import org.example.project.details.domain.RepoDetails

class DetailsDataToDomain : DetailsData.Mapper<RepoDetails> {
    override fun map(detailsData: DetailsData): RepoDetails = with(detailsData) {
        RepoDetails(
            repoOwner = repoOwner,
            repoName = repoName,
            repoDesc = repoDesc,
            forksCount = forksCount,
            issuesCount = issuesCount,
            programmingLanguage = programmingLanguage,
        )
    }
}

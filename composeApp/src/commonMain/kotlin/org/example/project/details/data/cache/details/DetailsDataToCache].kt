package org.example.project.details.data.cache.details

import org.example.project.details.data.DetailsData

class DetailsDataToCache : DetailsData.Mapper<DetailsCache> {
    override fun map(detailsData: DetailsData): DetailsCache = with(detailsData) {
        return DetailsCache(
            repoOwner = repoOwner,
            repoName = repoName,
            repoDesc = repoDesc,
            forksCount = forksCount,
            issuesCount = issuesCount,
            programmingLanguage = programmingLanguage,
        )
    }
}

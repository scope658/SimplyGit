package org.example.project.details.data.cache.details

import org.example.project.details.data.DetailsData

class DetailsCacheToData : DetailsCache.Mapper<DetailsData> {
    override fun map(detailsCache: DetailsCache): DetailsData = with(detailsCache) {
        return DetailsData(
            repoOwner = repoOwner,
            repoName = repoName,
            repoDesc = repoDesc,
            forksCount = forksCount,
            issuesCount = issuesCount,
            programmingLanguage = programmingLanguage,
        )
    }
}

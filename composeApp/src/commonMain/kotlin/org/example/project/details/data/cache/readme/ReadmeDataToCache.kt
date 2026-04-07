package org.example.project.details.data.cache.readme

import org.example.project.details.data.ReadmeData

class ReadmeDataToCache : ReadmeData.Mapper<ReadmeCache> {
    override fun map(readmeData: ReadmeData): ReadmeCache = with(readmeData) {
        return ReadmeCache(
            repoOwner = repoOwner,
            repoName = repoName,
            readme = readme,
        )
    }
}

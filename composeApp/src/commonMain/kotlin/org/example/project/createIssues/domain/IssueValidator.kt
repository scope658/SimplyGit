package org.example.project.createIssues.domain

class IssueValidator(
    private val resource: IssuesManageResource,

    ) {
    suspend fun validate(issue: Issue): IssueResult? {
        val titleLength = issue.title.length
        val bodyLength = issue.body.length
        return when {
            titleLength > 256 && bodyLength > 65536 ->
                IssueResult.AllLong(
                    resource.longTitle(currentLongTitleLength = titleLength),
                    resource.longBody(currentBodyLength = bodyLength)
                )

            titleLength > 256 ->
                IssueResult.LongTitle(resource.longTitle(titleLength))

            bodyLength > 65536 ->
                IssueResult.LongBody(resource.longBody(bodyLength))

            else -> null
        }
    }
}


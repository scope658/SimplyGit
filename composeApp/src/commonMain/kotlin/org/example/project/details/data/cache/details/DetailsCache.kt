package org.example.project.details.data.cache.details

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "details_table", primaryKeys = ["repo_owner", "repo_name"])
data class DetailsCache(

    @ColumnInfo(name = "repo_owner")
    val repoOwner: String,

    @ColumnInfo(name = "repo_name")
    val repoName: String,
    val repoDesc: String,
    val forksCount: Int,
    val issuesCount: Int,
    val programmingLanguage: String,
) {
    interface Mapper<T> {
        fun map(detailsCache: DetailsCache): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }

    companion object {
        val EMPTY = DetailsCache("", "", "", 0, 0, "")

    }
}

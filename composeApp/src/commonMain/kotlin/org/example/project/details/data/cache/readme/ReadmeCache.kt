package org.example.project.details.data.cache.readme

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "readme_table", primaryKeys = ["repo_owner", "repo_name"])
data class ReadmeCache(
    @ColumnInfo("repo_owner")
    val repoOwner: String,
    @ColumnInfo("repo_name")
    val repoName: String,
    val readme: String,
)

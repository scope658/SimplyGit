package org.example.project.core.presentation

interface RouteArgs {
    fun repoOwner(): String
    fun repoName(): String

    interface Path {
        fun path(): String
    }

    interface All : RouteArgs, Path
}

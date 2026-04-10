package org.example.project.createIssues.presentation

interface IssuesActions {
    fun create()
    fun onTitleChanged(title: String)
    fun onBodyChanged(body: String)
}

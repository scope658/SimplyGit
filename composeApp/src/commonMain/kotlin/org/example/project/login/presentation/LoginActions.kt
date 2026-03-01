package org.example.project.login.presentation

interface LoginActions {
    fun onUsernameChanged(userName: String)
    fun onPasswordChanged(password: String)
    fun login()
}
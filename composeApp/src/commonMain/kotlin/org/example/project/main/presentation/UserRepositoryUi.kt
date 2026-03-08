package org.example.project.main.presentation

import org.example.project.CommonParcelable
import org.example.project.CommonParcelize

@CommonParcelize
data class UserRepositoryUi(
    val id: Int,
    val userPhotoImageUrl: String,
    val userName: String,
    val repositoryName: String,
    val programmingLanguage: String,
    val stars: Int,
) : CommonParcelable

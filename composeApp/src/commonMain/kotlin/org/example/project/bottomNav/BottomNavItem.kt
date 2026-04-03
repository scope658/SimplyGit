package org.example.project.bottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.favourites_label
import ktshwnumbertwo.composeapp.generated.resources.profile_label
import ktshwnumbertwo.composeapp.generated.resources.search_label
import org.jetbrains.compose.resources.StringResource


sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: StringResource
) {

    object Search :
        BottomNavItem(
            route = "search",
            icon = Icons.Default.Search,
            labelRes = Res.string.search_label,
        )

    object Favourites :

        BottomNavItem(
            route = "favourites",
            icon = Icons.Filled.Favorite,
            labelRes = Res.string.favourites_label,
        )

    object Profile :
        BottomNavItem(
            route = "profile",
            icon = Icons.Filled.Person,
            labelRes = Res.string.profile_label,

            )

}

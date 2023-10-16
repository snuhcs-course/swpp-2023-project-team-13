package com.team13.fooriend.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
){
    object Home: BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = "home"
    )
    object Social: BottomNavItem(
        title = "Social",
        icon = Icons.Default.Person,
        route = "social"
    )
    object MyPage: BottomNavItem(
        title = "MyPage",
        icon = Icons.Default.AccountCircle,
        route = "myPage"
    )
}

package com.team13.fooriend.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon
import com.team13.fooriend.ui.fooriendicon.Home
import com.team13.fooriend.ui.fooriendicon.My
import com.team13.fooriend.ui.fooriendicon.Social

sealed class BottomNavItem( // Bottom Navigation Bar에 들어갈 아이템들
    val title: String,
    val icon: ImageVector,
    val route: String,
    val iconSize: Dp = 28.dp
){
    object Home: BottomNavItem(
        title = "Home",
        icon = FooriendIcon.Home,
        route = "home",
    )
    object Social: BottomNavItem(
        title = "Social",
        icon = Icons.Default.Search,
        route = "social"
    )
    object MyPage: BottomNavItem(
        title = "MyPage",
        icon = FooriendIcon.My,
        route = "myPage"
    )
}
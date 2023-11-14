package com.team13.fooriend.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.team13.fooriend.ui.MyIconPack
import com.team13.fooriend.ui.myiconpack.Home
import com.team13.fooriend.ui.myiconpack.My
import com.team13.fooriend.ui.myiconpack.Social

sealed class BottomNavItem( // Bottom Navigation Bar에 들어갈 아이템들
    val title: String,
    val icon: ImageVector,
    val route: String
){
    object Home: BottomNavItem(
        title = "Home",
        icon = MyIconPack.Home,
        route = "home"
    )
    object Social: BottomNavItem(
        title = "Social",
        icon = MyIconPack.Social,
        route = "social"
    )
    object MyPage: BottomNavItem(
        title = "MyPage",
        icon = MyIconPack.My,
        route = "myPage"
    )
}

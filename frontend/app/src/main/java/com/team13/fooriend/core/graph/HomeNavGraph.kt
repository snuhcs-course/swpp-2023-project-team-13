package com.team13.fooriend.core.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.screen.home.HomeScreen
import com.team13.fooriend.ui.screen.mypage.MyPageScreen
import com.team13.fooriend.ui.screen.social.SocialScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(nickname = "admin")
        }
        composable(route = BottomNavItem.Social.route) {
            SocialScreen()
        }
        composable(route = BottomNavItem.MyPage.route) {
            MyPageScreen()
        }
    }
}
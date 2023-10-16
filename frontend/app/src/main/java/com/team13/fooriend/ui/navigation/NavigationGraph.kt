package com.team13.fooriend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team13.fooriend.ui.screen.home.HomeScreen
import com.team13.fooriend.ui.screen.login.LogInScreen
import com.team13.fooriend.ui.screen.mypage.MyPageScreen
import com.team13.fooriend.ui.screen.signup.SignUpScreen
import com.team13.fooriend.ui.screen.social.SocialScreen

@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "home"){
        composable("login"){
            LogInScreen()
        }
        composable("signup"){
            SignUpScreen()
        }
        composable(BottomNavItem.Home.route){
            HomeScreen("admin")
        }
        composable(BottomNavItem.Social.route){
            SocialScreen()
        }
        composable(BottomNavItem.MyPage.route){
            MyPageScreen()
        }
    }
}
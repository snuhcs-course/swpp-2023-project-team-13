package com.team13.fooriend.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.screen.home.HomeScreen
import com.team13.fooriend.ui.screen.login.LogInScreen
import com.team13.fooriend.ui.screen.mypage.MyPageScreen
import com.team13.fooriend.ui.screen.signup.SignUpScreen
import com.team13.fooriend.ui.screen.social.SocialScreen


@Composable
fun FooriendApp() {
    val navController = rememberNavController()
    FooriendNavHost(navController = navController)
}

@Composable
fun FooriendNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LogInScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("home/{nickname}") {
                backStackEntry ->
            HomeScreen(
                navController = navController,
                nickname = backStackEntry.arguments?.getString("nickname") ?: "",
            )
        }
        composable("home") {
            HomeScreen(navController, "admin")
        }
        composable("social") {
            SocialScreen(navController)
        }
        composable("myPage") {
            MyPageScreen(navController)
        }
    }
}

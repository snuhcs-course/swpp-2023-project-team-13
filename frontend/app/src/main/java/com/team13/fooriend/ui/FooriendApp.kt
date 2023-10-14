package com.team13.fooriend.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.screen.home.HomePage
import com.team13.fooriend.ui.screen.login.LogInPage
import com.team13.fooriend.ui.screen.mypage.MyPage
import com.team13.fooriend.ui.screen.search.SearchPage
import com.team13.fooriend.ui.screen.signup.SignUpPage


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
            LogInPage(navController)
        }
        composable("signup") {
            SignUpPage(navController)
        }
        composable("home/{nickname}") {
                backStackEntry ->
            HomePage(
                navController = navController,
                nickname = backStackEntry.arguments?.getString("nickname") ?: "",
            )
        }
        composable("home") {
            HomePage(navController, "admin")
        }
        composable("search") {
            SearchPage(navController)
        }
        composable("my") {
            MyPage(navController)
        }
    }
}

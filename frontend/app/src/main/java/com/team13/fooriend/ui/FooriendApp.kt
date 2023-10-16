package com.team13.fooriend.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.component.BottomBar
import com.team13.fooriend.ui.screen.login.LogInScreen
import com.team13.fooriend.ui.screen.signup.SignUpScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooriendApp() {
    val navController = rememberNavController()
    //FooriendNavHost(navController = navController)
    BottomBar(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooriendNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LogInScreen()
        }
        composable("signup") {
            SignUpScreen()
        }
        composable("home"){

        }
    }
}

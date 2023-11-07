package com.team13.fooriend.core.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team13.fooriend.ui.screen.login.LogInScreen
import com.team13.fooriend.ui.screen.signup.SignUpScreen

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Forgot : AuthScreen(route = "FORGOT")
}
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route // default screen
    ){
        composable(route = AuthScreen.Login.route){// LogInScreen
            LogInScreen(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                },
                onSignUpClick = {
                    navController.navigate(AuthScreen.SignUp.route)
                }
            )
        }
        composable(route = AuthScreen.SignUp.route){// SignUpScreen
            SignUpScreen(
                onSignUpClick = {
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }
    }
}
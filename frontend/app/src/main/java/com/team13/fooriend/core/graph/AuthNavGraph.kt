package com.team13.fooriend.core.graph

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team13.fooriend.ui.screen.login.LogInScreen
import com.team13.fooriend.ui.screen.signup.SignUpScreen
import com.team13.fooriend.ui.util.getAccessToken

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Forgot : AuthScreen(route = "FORGOT")
}
fun NavGraphBuilder.authNavGraph(
    context : Context,
    navController: NavHostController
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route // default screen
    ){
        composable(route = AuthScreen.Login.route){
//            if(isLoggedIn(context)){
//                navController.popBackStack()
//                navController.navigate(Graph.HOME)
//            }
            // LogInScreen
            LogInScreen(
                context = context,
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
                context = context,
                onSignUpClick = {
                    navController.navigate(AuthScreen.Login.route)
                },
                onBackClick = {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }
    }
}

private fun isLoggedIn(context: Context): Boolean {
    val accesstoken = getAccessToken(context)
    return accesstoken != ""
}
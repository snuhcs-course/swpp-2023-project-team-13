package com.team13.fooriend.core.graph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team13.fooriend.ui.util.LoginSuccess

@Composable
fun RootNavigationGraph(navController: NavHostController, context: Context) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION // 앱이 시작하면 authNavGraph 먼저 시작
    ) {
        authNavGraph(context = context, navController = navController)
        composable(route = Graph.HOME) {
            LoginSuccess(context) // 로그인 성공하면 Graph.Home 라우터 호출 -> LoginSuccess로 이동 (util package)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}
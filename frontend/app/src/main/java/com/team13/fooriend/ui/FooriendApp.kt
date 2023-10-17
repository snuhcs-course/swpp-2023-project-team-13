package com.team13.fooriend.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.core.graph.RootNavigationGraph


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooriendApp() {
    val navController = rememberNavController()
    //BottomBar(navController = navController)
    RootNavigationGraph(navController = navController)
}

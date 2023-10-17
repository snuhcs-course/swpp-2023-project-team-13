package com.team13.fooriend.ui.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.component.BottomBar


@Composable
fun LoginSuccess(navController: NavHostController = rememberNavController()) {
    BottomBar(navController = navController)
}
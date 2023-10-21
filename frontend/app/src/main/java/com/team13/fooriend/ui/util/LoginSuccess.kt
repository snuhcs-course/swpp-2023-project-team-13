package com.team13.fooriend.ui.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.component.BottomBar


@Composable
fun LoginSuccess(context: Context, navController: NavHostController = rememberNavController()) {

    BottomBar(context, navController = navController)
}
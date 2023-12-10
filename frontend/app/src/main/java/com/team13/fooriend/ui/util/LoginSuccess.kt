package com.team13.fooriend.ui.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.component.BottomBar


@Composable
fun LoginSuccess(context: Context, navController: NavHostController = rememberNavController()) {
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    showBottomBar = when (navBackStackEntry?.destination?.route){
        "restaurant/{restaurantId}" -> false
        "reviewDetail/{reviewId}" -> false
        "fooriend/{userId}" -> false
        "writeReview/{restaurantPlaceId}/{restaurantName}" -> false
        "logout" -> false
        "myInfo" -> false
        else -> true // in all other cases show the bottom bar
    }
    BottomBar(context, navController = navController, showBottomBar = showBottomBar) // bottom bar를 보여줄지 말지 결정
}
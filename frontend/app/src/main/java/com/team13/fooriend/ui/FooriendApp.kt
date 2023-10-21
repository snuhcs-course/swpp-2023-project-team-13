package com.team13.fooriend.ui

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.core.graph.RootNavigationGraph


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooriendApp(context: Context) {
    val navController = rememberNavController()
    RootNavigationGraph(navController = navController, context)
}

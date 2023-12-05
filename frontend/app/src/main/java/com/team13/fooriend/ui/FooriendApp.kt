package com.team13.fooriend.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.R
import com.team13.fooriend.core.graph.RootNavigationGraph
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.data.Review
import com.team13.fooriend.ui.screen.RestaurantDetailScreen
import com.team13.fooriend.ui.screen.ReviewDetailScreen
import com.team13.fooriend.ui.screen.social.SocialScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooriendApp(context: Context) {
    val navController = rememberNavController()
    RootNavigationGraph(navController = navController, context)
}
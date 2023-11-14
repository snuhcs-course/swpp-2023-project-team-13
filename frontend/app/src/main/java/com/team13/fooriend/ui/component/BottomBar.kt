package com.team13.fooriend.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
<<<<<<< Updated upstream
import com.team13.fooriend.ui.theme.BaseGreen
=======
import com.team13.fooriend.ui.theme.FooriendColor
>>>>>>> Stashed changes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(context: Context, navController: NavHostController, showBottomBar: Boolean) {
    Scaffold(
        // bottom bar를 화면에 먼저 생성
        bottomBar = { if(showBottomBar) BottomNavigation(navController = navController)  }
    ) {
        Box(Modifier.padding(it)){// bottom bar를 제외한 나머지 화면을 생성
            HomeNavGraph(context = context, navController = navController) // 나머지 화면은 HomeNavGraph에 던져준다.
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController){
    val items = listOf<BottomNavItem>(
        BottomNavItem.Home,
        BottomNavItem.Social,
        BottomNavItem.MyPage,
    )
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .height(70.dp)
            .graphicsLayer {
                clip = true
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                shadowElevation = 30f },
        ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route


        items.forEach { item ->
            val selected = item.route == navBackStackEntry.value?.destination?.route
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                selected = selected,

                alwaysShowLabel = false,

                onClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FooriendColor.FooriendGreen,
                    unselectedIconColor = FooriendColor.FooriendGrey,
                )
            )
        }
    }
}
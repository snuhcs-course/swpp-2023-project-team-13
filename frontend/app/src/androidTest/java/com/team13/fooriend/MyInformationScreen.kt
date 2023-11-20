package com.team13.fooriend

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyInformationScreen {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setUpNavHost(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(navController = navController, context = LocalContext.current, startDestination = BottomNavItem.MyPage.route)
        }
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("myInfoButton")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule
            .onNodeWithTag("myInfoButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("MyInfoScreenTest", route.toString())
    }
    @Test
    fun performClick_OnLogOutButton_navigatesToLoginScreen(){
        composeTestRule
            .onNodeWithText("로그아웃")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "logout")
    }

    @Test
    fun performClick_OnBackButton_navigatesToMyPageScreen(){
        composeTestRule
            .onNodeWithTag("backButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.backStack.last().destination.route
        Assert.assertEquals(route, BottomNavItem.MyPage.route)
    }
}
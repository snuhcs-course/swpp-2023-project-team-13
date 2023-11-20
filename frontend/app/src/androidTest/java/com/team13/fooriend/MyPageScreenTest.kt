package com.team13.fooriend

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.AuthScreen
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.core.graph.RootNavigationGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
import okhttp3.internal.wait
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageScreenTest {
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
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("MyPageScreenTest", route.toString())
    }
    @Test
    fun performClick_OnSettingButton_navigatesToMyInformationScreen(){
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
        Assert.assertEquals(route, "myInfo")
    }

}
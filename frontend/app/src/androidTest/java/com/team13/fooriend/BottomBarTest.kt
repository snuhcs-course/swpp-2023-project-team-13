package com.team13.fooriend

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.AuthScreen
import com.team13.fooriend.core.graph.RootNavigationGraph
import com.team13.fooriend.ui.component.BottomBar
import com.team13.fooriend.ui.component.BottomNavigation
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.util.checkForPermission
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BottomBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost(){
        // 위치 정보 공유 허가 코드 작성 필요
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            context = LocalContext.current
            BottomBar(navController = navController, context = context, showBottomBar = true)
        }
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("BottomBarTestLog", route.toString())
    }

    @Test
    fun verify_StartDestinationIsHomeScreen(){
        composeTestRule
            .onNodeWithText("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }
    @Test
    fun performClick_OnSocialIcon_navigatesToSocialScreen_startHomeScreen(){
        // Home -> Social
        composeTestRule
            .onNodeWithText("Home")
            .performClick()

        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Social.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnSocialIcon_navigatesToSocialScreen_startMyPageScreen(){
        // MyPage -> Social
        composeTestRule
            .onNodeWithText("MyPage")
            .performClick()

        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Social.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnMyPageIcon_navigatesToMyPageScreen_startHomeScreen(){
        // Home -> MyPage
        composeTestRule
            .onNodeWithText("Home")
            .performClick()

        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.MyPage.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsSelected()
    }

    @Test
    fun performClick_OnMyPageIcon_navigatesToMyPageScreen_startSocialScreen(){
        // Social -> MyPage
        composeTestRule
            .onNodeWithText("Social")
            .performClick()

        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.MyPage.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsSelected()
    }

    @Test
    fun performClick_OnHomeIcon_navigatesHomeScreen_startSocialScreen(){
        // Social -> Home
        composeTestRule
            .onNodeWithText("Social")
            .performClick()

        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Home.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnHomeIcon_navigatesHomeScreen_startMyPageScreen(){
        // MyPage -> Home
        composeTestRule
            .onNodeWithText("MyPage")
            .performClick()

        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Home.route)
        composeTestRule
            .onNodeWithText("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }
}
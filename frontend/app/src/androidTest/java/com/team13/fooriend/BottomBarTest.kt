package com.team13.fooriend

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.team13.fooriend.ui.component.BottomBar
import com.team13.fooriend.ui.navigation.BottomNavItem
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
        // 위치 정보 공유 허가 코드 작성 필요 -> 아래에 작성함
        composeTestRule.setContent {
            context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            BottomBar(navController = navController, context = context, showBottomBar = true)
        }
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("BottomBarTestLog", route.toString())
    }
    @Before
    fun setUpPermission(){
        // 위치 정보 공유 허가 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                "pm grant " + InstrumentationRegistry.getInstrumentation().targetContext.packageName
                        + " android.permission.ACCESS_FINE_LOCATION"
            )
        }
    }

    @Test
    fun verify_StartDestinationIsHomeScreen(){
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
    }
    @Test
    fun performClick_OnSocialIcon_navigatesToSocialScreen_startHomeScreen(){
        // Home -> Social
        composeTestRule
            .onNodeWithTag("Home")
            .performClick()

        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Social.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnSocialIcon_navigatesToSocialScreen_startMyPageScreen(){
        // MyPage -> Social
        composeTestRule
            .onNodeWithTag("MyPage")
            .performClick()

        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Social.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnMyPageIcon_navigatesToMyPageScreen_startHomeScreen(){
        // Home -> MyPage
        composeTestRule
            .onNodeWithTag("Home")
            .performClick()

        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.MyPage.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsSelected()
    }

    @Test
    fun performClick_OnMyPageIcon_navigatesToMyPageScreen_startSocialScreen(){
        // Social -> MyPage
        composeTestRule
            .onNodeWithTag("Social")
            .performClick()

        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.MyPage.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsSelected()
    }

    @Test
    fun performClick_OnHomeIcon_navigatesHomeScreen_startSocialScreen(){
        // Social -> Home
        composeTestRule
            .onNodeWithTag("Social")
            .performClick()

        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Home.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun performClick_OnHomeIcon_navigatesHomeScreen_startMyPageScreen(){
        // MyPage -> Home
        composeTestRule
            .onNodeWithTag("MyPage")
            .performClick()

        composeTestRule
            .onNodeWithTag("Home")
            .assertIsNotSelected()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Home.route)
        composeTestRule
            .onNodeWithTag("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithTag("Social")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithTag("MyPage")
            .assertIsNotSelected()
    }
}
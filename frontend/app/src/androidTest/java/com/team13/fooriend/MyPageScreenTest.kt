package com.team13.fooriend

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertHasClickAction
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
import com.team13.fooriend.ui.util.saveAccessToken
import okhttp3.internal.wait
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost(){
        composeTestRule.setContent {
            context = LocalContext.current
            saveAccessToken(context, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJtZWNoYW5pY2pvIiwiaWF0IjoxNzAxMjY3NTc5LCJleHAiOjE3MDkwNDM1Nzl9.QcaBr1w8RKQ_FkQ_Yce_J7fOn6TuvVhjl3H0jjMANmY")
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(navController = navController, context = context, startDestination = BottomNavItem.MyPage.route)
        }
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("reviewLazyGridItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onRoot().printToLog("MyPageScreenTest")
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("MyPageScreenTest", route.toString())
    }
    @Test
    fun performClick_OnSettingButton_navigatesToMyInformationScreen(){
        composeTestRule
            .onNodeWithTag("myInfoButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "myInfo")
    }

    @Test
    fun performClick_OnReviewLazyGridItem_navigatesToReviewDetailScreen(){
        composeTestRule
            .onAllNodesWithTag("reviewLazyGridItem")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "reviewDetail/{reviewId}")
    }

    @Test
    fun performClick_OnFollowersButton_showFollowersList(){
        composeTestRule
            .onNodeWithTag("followersButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("popup")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("popup")
            .assertIsDisplayed()
    }

    @Test
    fun performClick_OnFollowersButton_showFollowingList(){
        composeTestRule
            .onNodeWithTag("followingButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("popup")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("popup")
            .assertIsDisplayed()
    }

    @Test
    fun performClick_OnFollwerNameClick_navigatesToFooriendScreen(){
        composeTestRule
            .onNodeWithTag("followersButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("popup")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("userListItem")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "fooriend/{userId}")
    }
    @Test
    fun performClick_OnFollwingNameClick_navigatesToFooriendScreen(){
        composeTestRule
            .onNodeWithTag("followingButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("popup")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("userListItem")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "fooriend/{userId}")
    }
}
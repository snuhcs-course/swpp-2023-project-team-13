package com.team13.fooriend

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FooriendScreenTest {
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
        composeTestRule
            .onNodeWithTag("followingButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("popup")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("userListItem")[0]
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("reviewLazyGridItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun performClick_OnBackButton_navigatesToMyPageScreen(){
        composeTestRule
            .onNodeWithTag("backButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("FooriendScreenTest", route.toString())
    }

    @Test
    fun verify_FooriendFollowState(){
        composeTestRule
            .onNodeWithTag("profileSection")
            .assertIsDisplayed()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("unfollowButton")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("unfollowButton")
            .assertIsDisplayed()
    }

    @Test
    fun performClick_OnUnfollowButton_OnFollowButton(){
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("unfollowButton")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("unfollowButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("followButton")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("followButton")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun performClick_OnReviewLazyGridItem_navigatesToReviewDetailScreen(){
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("reviewLazyGridItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("reviewLazyGridItem")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "reviewDetail/{reviewId}")
    }
}
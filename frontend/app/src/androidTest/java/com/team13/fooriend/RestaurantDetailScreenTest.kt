package com.team13.fooriend

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost_fromReviewDetailScreen(){
        composeTestRule.setContent {
            context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(navController = navController, context = context, startDestination = BottomNavItem.Social.route)
        }
        composeTestRule.waitUntil() {
            composeTestRule
                .onAllNodesWithTag("reviewLazyGridItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("reviewLazyGridItem")[0]
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("reviewDetailScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("restaurantName")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("RestaurantDetailScreenTest", route.toString())
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag("restaurantDetailScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun performClick_OnBackButton_navigatesToReviewDetailScreen(){
        composeTestRule
            .onNodeWithTag("backButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "reviewDetail/{reviewId}")
    }

    @Test
    fun performClick_OnWriterName_navigatesToFooriendScreen(){
        composeTestRule
            .onAllNodesWithTag("writerName")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "fooriend/{userId}")
    }

    @Test
    fun performClick_OnIsLikeButton_SortReviewList(){
        composeTestRule
            .onNodeWithTag("isLikeButton")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun performClick_OnIsDislikeButton_SortReviewList(){
        composeTestRule
            .onNodeWithTag("isDislikeButton")
            .assertIsDisplayed()
            .performClick()
    }
}
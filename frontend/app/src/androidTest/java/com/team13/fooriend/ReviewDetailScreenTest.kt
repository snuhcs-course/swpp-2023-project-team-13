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
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReviewDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost_fromSocialScreen(){
        composeTestRule.setContent {
            context = LocalContext.current
            saveAccessToken(context, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJtZWNoYW5pY2pvIiwiaWF0IjoxNzAxMjY3NTc5LCJleHAiOjE3MDkwNDM1Nzl9.QcaBr1w8RKQ_FkQ_Yce_J7fOn6TuvVhjl3H0jjMANmY")
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(navController = navController, context = context, startDestination = BottomNavItem.Social.route)
        }
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("reviewLazyGridItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onAllNodesWithTag("reviewLazyGridItem")[0]
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("ReviewDetailScreenTest", route.toString())
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("reviewDetailScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun performClick_OnBackButton_navigatesToSocialScreen(){
        composeTestRule
            .onNodeWithTag("backButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, BottomNavItem.Social.route)
    }

    @Test
    fun performClick_writerName_navigatesToFooriendScreen(){
        composeTestRule
            .onNodeWithTag("writerName")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "fooriend/{userId}")
    }

    @Test
    fun performClick_restaurantName_navigatesToRestaurantDetailScreen(){
        composeTestRule
            .onNodeWithTag("restaurantName")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "restaurant/{restaurantId}")
    }

//    @Test
//    fun performClick_deleteButton

}
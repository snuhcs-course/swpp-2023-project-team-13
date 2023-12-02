package com.team13.fooriend

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost_fromReviewDetailScreen() {
        composeTestRule.setContent {
            context = LocalContext.current
            saveAccessToken(
                context,
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJtZWNoYW5pY2pvIiwiaWF0IjoxNzAxMjY3NTc5LCJleHAiOjE3MDkwNDM1Nzl9.QcaBr1w8RKQ_FkQ_Yce_J7fOn6TuvVhjl3H0jjMANmY"
            )
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(
                navController = navController,
                context = context,
                startDestination = BottomNavItem.Social.route
            )
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
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("reviewDetailScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("restaurantName")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("restaurantDetailScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("postingButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("PostingScreenTest", route.toString())
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithTag("postingScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun display_postingScreen(){
        composeTestRule
            .onNodeWithTag("postingScreen")
            .assertIsDisplayed()
    }

    @Test
    fun display_writeReviewField(){
        composeTestRule
            .onNodeWithTag("writeReviewField")
            .assertIsDisplayed()
            .performTextInput("test")
        composeTestRule
            .onNodeWithTag("writeReviewField")
            .assertTextEquals("test")
    }
    @Test
    fun display_postingButton(){
        composeTestRule
            .onNodeWithTag("postingButton")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
    @Test
    fun display_imageButton(){
        composeTestRule
            .onNodeWithTag("imageButton")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun display_receiptButton(){
        composeTestRule
            .onNodeWithTag("receiptButton")
            .assertIsDisplayed()
            .assertHasClickAction()
    }


    @Test
    fun performClick_OnBackButton_navigatesToRestaurantDetailScreen(){
        composeTestRule
            .onNodeWithTag("backButton")
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "restaurant/{restaurantId}")
    }



}
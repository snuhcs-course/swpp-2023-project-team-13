package com.team13.fooriend

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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BottomBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setUpNavHost(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            BottomBar(navController = navController, context = LocalContext.current, showBottomBar = true)
        }
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
    // 이후부터는 스마트폰 필요
    @Test
    fun performClick_OnSocialIcon_navigatesToSocialScreen(){
        composeTestRule
            .onNodeWithText("Social")
            .performClick()
    }

    @Test
    fun performClick_OnMyPageIcon_navigatesToMyPageScreen(){
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }

    @Test
    fun test3(){
        composeTestRule
            .onNodeWithText("MyPage")
            .assertIsNotSelected()
    }



}
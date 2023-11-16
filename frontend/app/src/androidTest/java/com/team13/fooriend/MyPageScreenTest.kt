package com.team13.fooriend

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.AuthScreen
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.core.graph.RootNavigationGraph
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
            HomeNavGraph(navController = navController, context = LocalContext.current)

        }
    }

    @Test
    fun performClick_OnSettingButton_navigatesToMyInformationScreen(){
        composeTestRule
            .onNodeWithContentDescription("My Info")

        composeTestRule.onRoot().printToLog("test1234")
    }

}
package com.team13.fooriend

import android.content.Context
import androidx.annotation.UiThread
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.AuthScreen
import com.team13.fooriend.core.graph.Graph
import com.team13.fooriend.core.graph.RootNavigationGraph
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var context: Context
    @Before
    fun setUpNavHost(){
        composeTestRule.setContent {
            context = LocalContext.current
            saveAccessToken(context, "")
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            RootNavigationGraph(navController = navController, context = context)
        }
    }

    @Test
    fun verify_StartDestinationIsLoginScreen(){
        composeTestRule
            .onNodeWithText("LOGIN")
            .assertIsDisplayed()
    }

    @Test
    fun performClick_OnSignUpButton_navigatesToSignUpScreen(){
        composeTestRule
            .onNodeWithText("SIGN UP")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, AuthScreen.SignUp.route)
    }

    @Test
    fun performClick_OnLogInButton_withEmptyIDandPWD(){
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, AuthScreen.Login.route)
    }

    @Test
    fun performClick_OnLogInButton_withWrongIDorPWD(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("admin")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("admin") // please change wrong pwd
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, AuthScreen.Login.route)
    }

    @Test
    fun performClick_OnLogInButton_withCorrectIDandPWD(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("mechanicjo")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("1q2w3e4r")
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        composeTestRule.runOnUiThread {
            val route = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(route, AuthScreen.Login.route)
        }
    }
}
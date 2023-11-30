package com.team13.fooriend

import android.content.Context
import android.util.Log
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
import okhttp3.internal.wait
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
    fun performClick_OnLogInButton_withWrongIDorPWD1(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("admin")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("admin")
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, AuthScreen.Login.route)
    }
    @Test
    fun performClick_OnLogInButton_withWrongIDorPWD2(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("mechanicjo")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("1q2w3e4")
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, AuthScreen.Login.route)
    }

    @Test
    fun performClick_OnLogInButton_withCorrectIDandPWD1(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("mechanicjo")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("1q2w3e4r")
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        composeTestRule.waitUntil(10000) {
            navController.currentBackStackEntry?.destination?.route == Graph.HOME
        }
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, Graph.HOME)
        Log.d("testLog", context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("AccessToken", null).toString())
    }

    @Test
    fun performClick_OnLogInButton_withCorrectIDandPWD2(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("jakehsj")
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("1q2w3e4r")
        composeTestRule
            .onNodeWithText("LOGIN")
            .performClick()
        composeTestRule.waitUntil(10000) {
            navController.currentBackStackEntry?.destination?.route == Graph.HOME
        }
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, Graph.HOME)
        Log.d("testLog", context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("AccessToken", null).toString())
    }
}
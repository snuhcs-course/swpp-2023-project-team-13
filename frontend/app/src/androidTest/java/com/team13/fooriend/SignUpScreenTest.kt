package com.team13.fooriend

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.team13.fooriend.core.graph.RootNavigationGraph
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
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
        composeTestRule
            .onNodeWithText("SIGN UP")
            .performClick()
    }

    @Test
    fun display_NameTextField(){
        composeTestRule
            .onNodeWithText("NAME")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("nameTextField")
            .assertIsDisplayed()
    }
    @Test
    fun display_IDTextField(){
        composeTestRule
            .onNodeWithText("ID")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("idTextField")
            .assertIsDisplayed()
    }

    @Test
    fun display_PasswordTextField(){
        composeTestRule
            .onNodeWithText("PASSWORD")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("passwordTextField")
            .assertIsDisplayed()
    }

    @Test
    fun display_PasswordCheckTextField(){
        composeTestRule
            .onNodeWithText("PASSWORD CONFIRM")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("passwordCheckTextField")
            .assertIsDisplayed()
    }

    @Test
    fun display_SignUpButton(){
        composeTestRule
            .onNodeWithText("SIGN UP")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun verify_StartDestinationIsSignUpScreen(){
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "SIGN_UP")
    }

    @Test
    fun canTextInput_OnNameTextField(){
        composeTestRule
            .onNodeWithText("NAME")
            .performTextInput("NAME_test")
        composeTestRule
            .onNodeWithTag("nameTextField")
            .assertIsDisplayed()
            .assertTextEquals("NAME_test")
    }

    @Test
    fun canTextInput_OnIDTextField(){
        composeTestRule
            .onNodeWithText("ID")
            .performTextInput("ID_test")
        composeTestRule
            .onNodeWithTag("idTextField")
            .assertIsDisplayed()
            .assertTextEquals("ID_test")
    }

    @Test
    fun canTextInput_OnPasswordTextField(){
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performTextInput("PASSWORD_test")
        composeTestRule
            .onNodeWithTag("passwordTextField")
            .assertIsDisplayed()
            .assertTextEquals("PASSWORD_test")
    }

    @Test
    fun canTextInput_OnPasswordCheckTextField(){
        composeTestRule
            .onNodeWithText("PASSWORD CONFIRM")
            .performTextInput("PASSWORD_CHECK_test")
        composeTestRule
            .onNodeWithTag("passwordCheckTextField")
            .assertIsDisplayed()
            .assertTextEquals("PASSWORD_CHECK_test")
    }

    @Test
    fun performClick_OnSignUpButton_withEmptyFields(){
        composeTestRule
            .onNodeWithText("SIGN UP")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "SIGN_UP")
    }

    @Test
    fun performClick_OnSignUpButton_withPasswordAndPasswordConfirmDoesNotMatches(){
        composeTestRule
            .onNodeWithTag("nameTextField")
            .performTextInput("NAME_test")
        composeTestRule
            .onNodeWithTag("idTextField")
            .performTextInput("ID_test")
        composeTestRule
            .onNodeWithTag("passwordTextField")
            .performTextInput("PASSWORD_test")
        composeTestRule
            .onNodeWithTag("passwordCheckTextField")
            .performTextInput("PASSWORD_CHECK_test")
        composeTestRule
            .onNodeWithText("SIGN UP")
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route, "SIGN_UP")
    }

}
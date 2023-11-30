package com.team13.fooriend

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.team13.fooriend.core.graph.HomeNavGraph
import com.team13.fooriend.ui.component.BottomBar
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.screen.home.PlacesApiService
import com.team13.fooriend.ui.util.saveAccessToken
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setUpNavHost(){
        composeTestRule.setContent {
            context = LocalContext.current
            saveAccessToken(context, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJtZWNoYW5pY2pvIiwiaWF0IjoxNzAxMjY3NTc5LCJleHAiOjE3MDkwNDM1Nzl9.QcaBr1w8RKQ_FkQ_Yce_J7fOn6TuvVhjl3H0jjMANmY")
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeNavGraph(navController = navController, context = context, startDestination = BottomNavItem.Home.route)
        }
        val route = navController.currentBackStackEntry?.destination?.route
        Log.d("HomeScreenTestLog", route.toString())
    }
    @Before
    fun setUpPermission(){
        // 위치 정보 공유 허가 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                "pm grant " + InstrumentationRegistry.getInstrumentation().targetContext.packageName
                        + " android.permission.ACCESS_FINE_LOCATION"
            )
        }
    }

//    @Before
//    suspend fun setUpPlaceAPI(){
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://maps.googleapis.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val placesApi = retrofit.create(PlacesApiService::class.java)
//        // 숙이네 조개전골 : ChIJeRbccCegfDURB6X6f-EULwQ (음식점 테스트용)
//        // 하이보 : ChIJS61IbouffDURzCrpD_a-s2s (음식점 테스트용)
//        // 별빛스테이션 : ChIJr5IDLh6ffDURs1nTu8Zlhps (비음식점 테스트용)
//        val isRestaurant_1 = placesApi.getPlaceDetails(
//            placeId = "ChIJeRbccCegfDURB6X6f-EULwQ",
//            apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0"
//        )
//        val isRestaurant_2 = placesApi.getPlaceDetails(
//            placeId = "ChIJS61IbouffDURzCrpD_a-s2s",
//            apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0"
//        )
//        val isNotRestaurant_1 = placesApi.getPlaceDetails(
//            placeId = "ChIJr5IDLh6ffDURs1nTu8Zlhps",
//            apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0"
//        )
//    }

    @Test
    fun verify_StartDestinationIsHomeScreen(){
        composeTestRule
            .onNodeWithTag("HomeScreen")
            .assertIsDisplayed()
    }

}
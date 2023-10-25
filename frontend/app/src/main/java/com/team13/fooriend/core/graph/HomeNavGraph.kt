package com.team13.fooriend.core.graph

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.data.Review
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.screen.RestaurantDetailScreen
import com.team13.fooriend.ui.screen.ReviewDetailScreen
import com.team13.fooriend.ui.screen.home.HomeScreen
import com.team13.fooriend.ui.screen.mypage.MyInformationScreen
import com.team13.fooriend.ui.screen.mypage.MyPageScreen
import com.team13.fooriend.ui.screen.social.SocialScreen

@Composable
fun HomeNavGraph(
    context: Context,
    navController: NavHostController,
) {
    // example data
    val review = Review(id = 1, writerId = 1, restaurantId = 1, content = "탕수육이 진짜 바삭!!, 여기 진짜 짬뽕 맛집이예요 별점 10개도 부족합니다.",
        image = listOf(R.drawable.hamburger, R.drawable.profile_cat, R.drawable.hamburger),
        confirm = true, title = "title")
    val restaurant = Restaurant(id = 1, name = "용찬반점", latitude = 1.0, longitude = 1.0,
        reviewCount = 3, reviewList = listOf(review.id, review.id, review.id), good = 1, bad = 1)
    val activity = (LocalContext.current as Activity)

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(nickname = "admin", context)
        }
        composable(route = BottomNavItem.Social.route) {
            SocialScreen(
                onReviewClick = {
                    navController.navigate("reviewDetail/${it}") },
            )
        }
        composable(route = BottomNavItem.MyPage.route) {
            MyPageScreen(
                onMyInfoClick = { navController.navigate("myInfo") },
            )
        }
        composable(route = "restaurant/{restaurantId}"){
            RestaurantDetailScreen(
                restaurant = restaurant,
                onCloseClick = { navController.navigateUp() },
                onWriteReviewClick = { navController.navigate("writeReview") },
                onWriterClick = { navController.navigate("fooriend/${it}") },
            )
        }
        composable("reviewDetail/{reviewId}"){backStackEntry ->
            ReviewDetailScreen(
                onBackClick = { navController.navigateUp() },
                onWriterClick = { navController.navigate("fooriend/${it}") },
                onRestaurantClick = { navController.navigate("restaurant/${it}") },
                reviewId = backStackEntry.arguments?.getString("reviewId")?.toInt() ?: 0,
            )
        }
        composable(route = "fooriend"){

        }
        composable(route = "writeReview"){
            // WriteReviewScreen(
            //     onBackClick = { navController.navigateUp() },
            // )
        }
        composable(route = "myInfo"){
            MyInformationScreen(
                onBackClick = { navController.navigateUp() },
            )
        }

    }
}
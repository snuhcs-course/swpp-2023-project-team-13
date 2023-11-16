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
import com.team13.fooriend.ui.FooriendApp
import com.team13.fooriend.ui.navigation.BottomNavItem
import com.team13.fooriend.ui.screen.FooriendScreen
import com.team13.fooriend.ui.screen.PostingScreen
import com.team13.fooriend.ui.screen.RestaurantDetailScreen
import com.team13.fooriend.ui.screen.ReviewDetailScreen
import com.team13.fooriend.ui.screen.home.HomeScreen
import com.team13.fooriend.ui.screen.mypage.ChangePwdScreen
import com.team13.fooriend.ui.screen.mypage.ChangePwdScreenPreview
import com.team13.fooriend.ui.screen.mypage.MyInformationScreen
import com.team13.fooriend.ui.screen.mypage.MyPageScreen
import com.team13.fooriend.ui.screen.social.SocialScreen
import com.team13.fooriend.ui.util.saveAccessToken

@Composable
fun HomeNavGraph(
    context: Context,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomNavItem.Home.route // login이 성공하면 home 화면으로 이동
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                nickname = "admin",
                context,
                onReviewClick = { navController.navigate("restaurant/${it}") }, // 지도에서 restaurant를 클릭한 경우
            )
        }
        composable(route = BottomNavItem.Social.route) {
            SocialScreen(
                context = context,
                onReviewClick = {
                    navController.navigate("reviewDetail/${it}") }, //  리뷰 이미지를 클릭한 경우
                onUserClick = { navController.navigate("fooriend/${it}") }, // search bar에서 검색한 유저를 클릭한 경우
            )
        }
        composable(route = BottomNavItem.MyPage.route) {
            MyPageScreen(
                context = context,
                onMyInfoClick = { navController.navigate("myInfo") },
                onReviewClick = { navController.navigate("reviewDetail/${it}") }, // 리뷰 이미지를 클릭한 경우
            )
        }
        composable(route = "restaurant/{restaurantId}"){
            RestaurantDetailScreen(
                context = context,
                restaurantPlaceId = it.arguments?.getString("restaurantId")?:"",
                onBackClick = { navController.navigateUp() }, // 뒤로가기 버튼을 누른 경우
                onWriteReviewClick = { navController.navigate("writeReview/${it}") }, // 리뷰 작성 버튼을 누른 경우
                onWriterClick = { navController.navigate("fooriend/${it}") }, // 리뷰에 있는 작성자 프로필 이미지를 누른 경우
            )
        }
        composable("reviewDetail/{reviewId}"){backStackEntry ->
            ReviewDetailScreen(
                context = context,
                onBackClick = { navController.navigateUp() },
                onWriterClick = { navController.navigate("fooriend/${it}") }, // 작성자를 클릭한 경우
                onRestaurantClick = { navController.navigate("restaurant/${it}") }, // 식당을 클릭한 경우
                reviewId = backStackEntry.arguments?.getString("reviewId")?.toInt() ?: 0,
            )
        }
        composable(route = "fooriend/{userId}"){
            FooriendScreen(
                context = context,
                onBackClick = { navController.navigateUp() },
                onFollowClick = { }, //TODO: Follow 버튼을 누르면 팔로우가 되도록 구현해야 함
                userId = it.arguments?.getString("userId")?.toInt() ?: 0,
                onReviewClick = { navController.navigate("reviewDetail/${it}") }, // 리뷰 이미지를 클릭한 경우
            )
        }
        composable(route = "writeReview/{restaurantPlaceId}"){
            PostingScreen(
                context = context,
                restaurantPlaceId = it.arguments?.getString("restaurantPlaceId")?:"",
                onCloseClick = { navController.navigateUp() },
                onPostClick = { navController.navigateUp() }, // TODO : Post버튼을 누르면 review가 저장되어야 함
            )
        }
        composable(route = "myInfo"){
            MyInformationScreen(
                context = context,
                onBackClick = { navController.navigateUp() },
                onChangePwd = {
                    saveAccessToken(context, "")
                    navController.navigate("changePwd") },
            )
        }
        composable(route = "changePwd"){
             FooriendApp(context = context)
        }
    }
}
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

@Composable
fun HomeNavGraph(
    context: Context,
    navController: NavHostController,
) {
    // example data
    val review1 = Review(id = 1, writerId = 1, restaurantId = 1, content = "탕수육이 진짜 바삭!!, 여기 진짜 짬뽕 맛집이예요 별점 10개도 부족합니다.",
        image = listOf(R.drawable.tangsuyug, R.drawable.jjambbong, R.drawable.jjambbong),
        confirm = true, title = "title")
    val review2 = Review(id = 2, writerId = 1, restaurantId = 1, content = "이 집 짜장이 기가 막히네",
        image = listOf(R.drawable.jjajangmyeon),
        confirm = true, title = "title")
    val review3 = Review(id = 3, writerId = 1, restaurantId = 1, content = "이 집 고양이 때문에 심장이 너무 아팠습니다.. ㅠㅠ",
        image = listOf(R.drawable.profile_cat),
        confirm = true, title = "title")
    val restaurant = Restaurant(id = 1, name = "용찬반점", latitude = 1.0, longitude = 1.0,
        reviewCount = 3, reviewList = listOf(review1.id, review2.id, review3.id), good = 2, bad = 1)

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
                onReviewClick = {
                    navController.navigate("reviewDetail/${it}") }, //  리뷰 이미지를 클릭한 경우
                onUserClick = { navController.navigate("fooriend/${it}") }, // search bar에서 검색한 유저를 클릭한 경우
            )
        }
        composable(route = BottomNavItem.MyPage.route) {
            MyPageScreen(
                onMyInfoClick = { navController.navigate("myInfo") },
                onReviewClick = { navController.navigate("reviewDetail/${it}") }, // 리뷰 이미지를 클릭한 경우
            )
        }
        composable(route = "restaurant/{restaurantId}"){
            RestaurantDetailScreen(
                restaurantPlaceId = it.arguments?.getString("restaurantId")?:"",
                onBackClick = { navController.navigateUp() }, // 뒤로가기 버튼을 누른 경우
                onWriteReviewClick = { navController.navigate("writeReview") }, // 리뷰 작성 버튼을 누른 경우
                onWriterClick = { navController.navigate("fooriend/${it}") }, // 리뷰에 있는 작성자 프로필 이미지를 누른 경우
            )
        }
        composable("reviewDetail/{reviewId}"){backStackEntry ->
            ReviewDetailScreen(
                onBackClick = { navController.navigateUp() },
                onWriterClick = { navController.navigate("fooriend/${it}") }, // 작성자를 클릭한 경우
                onRestaurantClick = { navController.navigate("restaurant/${it}") }, // 식당을 클릭한 경우
                reviewId = backStackEntry.arguments?.getString("reviewId")?.toInt() ?: 0,
            )
        }
        composable(route = "fooriend/{userId}"){
            FooriendScreen(
                onBackClick = { navController.navigateUp() },
                onFollowClick = { }, //TODO: Follow 버튼을 누르면 팔로우가 되도록 구현해야 함
                userId = it.arguments?.getString("userId")?.toInt() ?: 0,
            )
        }
        composable(route = "writeReview"){
            PostingScreen(
                onCloseClick = { navController.navigateUp() },
                onPostClick = { navController.navigateUp() }, // TODO : Post버튼을 누르면 review가 저장되어야 함
            )
        }
        composable(route = "myInfo"){
            MyInformationScreen(
                onBackClick = { navController.navigateUp() },
                onChangePwd = { navController.navigate("changePwd") },
            )
        }
        composable(route = "changePwd"){
             ChangePwdScreen(
                 onConfirmClick = { navController.navigateUp() },
             )
        }
    }
}
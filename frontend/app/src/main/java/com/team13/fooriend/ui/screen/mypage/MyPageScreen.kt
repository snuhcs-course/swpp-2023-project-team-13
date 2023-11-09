package com.team13.fooriend.ui.screen.mypage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.R
import com.team13.fooriend.ui.component.ProfileSection
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun MyPageScreen(
    onMyInfoClick: () -> Unit,
    onReviewClick: (Int) -> Unit
){
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("")}
    var followerCount by remember { mutableStateOf(0) }
    var followingCount by remember { mutableStateOf(0) }
    var userProfileImageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getMyReviews()
            val response2 = apiService.getMyInfo()
            reviews = response.reviewList
            username = response2.name
            followerCount = response2.followerCount
            followingCount = response2.followingCount
            userProfileImageUrl = response2.profileImage
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        username = reviews[0].user.name
        isLoading = false
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ){
            IconButton(onClick = onMyInfoClick){
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Back",
                    tint = Color.Black,
                )
            }
        }
        // 프로필 섹션
        ProfileSection(username = username,
            followersCount = followerCount,
            followingCount = followingCount,
            userProfileImageUrl = userProfileImageUrl,
            isMyPage = true)

        Spacer(modifier = Modifier.height(16.dp))


        // 중앙의 음식 리스트
        ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyPageScreenPreview(){
    MyPageScreen( onMyInfoClick = {  }, onReviewClick = {  })
}
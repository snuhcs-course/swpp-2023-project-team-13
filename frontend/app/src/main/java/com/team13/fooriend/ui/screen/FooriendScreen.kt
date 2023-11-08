package com.team13.fooriend.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.component.ProfileSection
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun FooriendScreen(
    onBackClick : () -> Unit = {},
    onFollowClick : () -> Unit = {},
    userId : Int = 0,
){
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getUserDetail(userId = userId)
            reviews = response.reviewList
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        isLoading = false
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }

        // profile section
        ProfileSection(username = reviews[0].user.name,  onFollowClick = onFollowClick)

        // review lazy grid
        ReviewLazyGrid(reviews = reviews, onReviewClick = { })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun FooriendScreenPreview(){
    FooriendScreen()
}


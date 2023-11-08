package com.team13.fooriend.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.ui.screen.home.Review
import com.team13.fooriend.ui.screen.home.ApiService
import com.team13.fooriend.ui.screen.home.MyItem
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantPlaceId: String,
    onBackClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onWriterClick: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Retrofit 설정
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    // LaunchedEffect를 사용하여 Composable이 처음 구성될 때 데이터 로드
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getRestaurantDetail(restaurantPlaceId = restaurantPlaceId)
            reviews = response.reviewList
        } catch (e: Exception) {
            // 예외 처리
        }
        isLoading = false
    }
    if(!isLoading) {
        Scaffold(
            // top bar를 설정하면서 top bar 영역은 scroll의 영향을 받지 않도록 한다.
            topBar = {
                TopRestaurantBar(
                    onCloseClick = onBackClick,
                    onWriteReviewClick = onWriteReviewClick,
                    restaurantName = reviews[0].restaurant.name,
                    restaurantGood = 1,
                    restaurantBad = 1,
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(reviews) { review -> // restaurant에 있는 reveiwList를 가져온다.
                    ReviewItem(
                        review = review,
                        onWriterClick = onWriterClick,
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review, onWriterClick: (Int) -> Unit) {
    // 실제로는 reviewId를 가지고 서버에서 받아와야 함
//    val review1 = Review(id = 1, writerId = 1, restaurantId = 1, content = "탕수육이 진짜 바삭!!, 여기 진짜 짬뽕 맛집이예요 별점 10개도 부족합니다.",
//        image = listOf(R.drawable.tangsuyug, R.drawable.jjambbong, R.drawable.jjambbong),
//        confirm = true, title = "title")
//    val review2 = Review(id = 2, writerId = 1, restaurantId = 1, content = "이 집 짜장이 기가 막히네",
//        image = listOf(R.drawable.jjajangmyeon),
//        confirm = true, title = "title")
//    val review3 = Review(id = 3, writerId = 1, restaurantId = 1, content = "이 집 고양이 때문에 심장이 너무 아팠습니다.. ㅠㅠ",
//        image = listOf(R.drawable.profile_cat),
//        confirm = true, title = "title")
    // reviewId에 따라 다른 review를 가져온다.
    val image = listOf(R.drawable.tangsuyug, R.drawable.jjambbong, R.drawable.jjambbong)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White)
            .padding(10.dp, 5.dp)
            .heightIn(min = 200.dp, max = 400.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
    ){
        LazyRow(){
            items(image){// review.image
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "review image",
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        IconButton(onClick = { onWriterClick(1) }) { // writerId
            Icon(
                imageVector = Icons.Default.AccountBox, // Default image가 아니라 user profile 이미지를 삽입해야 한다.
                contentDescription = "Writer",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 0.dp, 10.dp),
        ){
            Text(text = "title")
            Text(text = review.content)
        }
    }
}

@Composable
fun TopRestaurantBar(
    onCloseClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    restaurantName: String,
    restaurantGood: Int,
    restaurantBad: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ){
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    tint = Color.Black
                )
            }
        }
        Text(text = restaurantName)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            TextButton(onClick = { /*TODO*/ }) {// 좋아요 버튼 누르면 긍정 리뷰만 뜨도록
                Text(text = "좋아요 $restaurantGood")
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = { /*TODO*/ }) {// 싫어요 버튼 누르면 부정 리뷰만 뜨도록
                Text(text = "싫어요 $restaurantBad")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = onWriteReviewClick) {
                Text(text = "리뷰 작성")
            }
        }
    }
}

//@Composable
//@Preview(showSystemUi = true, showBackground = true)
//fun RestaurantDetailScreenPreview() {
//    RestaurantDetailScreen(
//        restaurant = Restaurant(
//            id = 0,
//            name = "음식점 이름",
//            good = 0,
//            bad = 0,
//            reviewList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
//            latitude = 0.0,
//            longitude = 0.0,
//            reviewCount = 10,
//        ),
//        onBackClick = {},
//        onWriteReviewClick = {},
//        onWriterClick = {},
//    )
//}
package com.team13.fooriend.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.screen.home.MyItem
import com.team13.fooriend.ui.util.createRetrofit
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    context: Context,
    restaurantPlaceId: String,
    onBackClick: () -> Unit,
    onWriteReviewClick: (String) -> Unit,
    onWriterClick: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Retrofit 설정
    val retrofit = createRetrofit(context)

    val apiService = retrofit.create(ApiService::class.java)
    var resPlaceId = restaurantPlaceId.substring(0,27)
    var restaurantName  = restaurantPlaceId.substring(27)
    var restaurantGood by remember { mutableStateOf(0) }
    var restaurantBad by remember { mutableStateOf(0) }

    // LaunchedEffect를 사용하여 Composable이 처음 구성될 때 데이터 로드
    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getRestaurantDetail(restaurantPlaceId = resPlaceId)
            reviews = response.reviewList
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
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
                    restaurantPlaceId = resPlaceId,
                    restaurantName = restaurantName,
                    restaurantGood = restaurantGood,
                    restaurantBad = restaurantBad,
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
                items(reviews) { review -> // restaurant에 있는 reveiwList를 가져옴
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
    val image = review.images
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
                LoadImageFromUrl(url = it.url)
            }
        }
        Row() {
            IconButton(onClick = { onWriterClick(review.user.id) }) { // review.user.id
                Icon(
                    imageVector = Icons.Default.AccountBox, // Default image가 아니라 user profile 이미지를 삽입해야 한다.
                    contentDescription = "Writer",
                    tint = Color.Black
                )
            }
            // at the middle of the row
            Text(text = review.user.name, modifier = Modifier.align(Alignment.CenterVertically))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 0.dp, 10.dp),
        ){
            Text(text = review.content)
        }
    }
}

@Composable
fun LoadImageFromUrl(url: String) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                crossfade(true)
                // 이곳에 더 많은 Coil 설정을 추가할 수 있습니다.
                // 예: placeholder(R.drawable.placeholder), error(R.drawable.error)
            },

            ),
        contentDescription = "Loaded image",
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop // 이미지를 어떻게 맞출지 정하는 옵션
    )
}

@Composable
fun TopRestaurantBar(
    onCloseClick: () -> Unit,
    onWriteReviewClick: (String) -> Unit,
    restaurantPlaceId: String,
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
            Button(onClick = { onWriteReviewClick(restaurantPlaceId) }) {
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
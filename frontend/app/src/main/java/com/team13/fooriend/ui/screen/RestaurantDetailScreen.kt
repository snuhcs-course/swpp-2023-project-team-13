package com.team13.fooriend.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.team13.fooriend.ui.FooriendIcon
import com.team13.fooriend.ui.fooriendicon.Dislike
import com.team13.fooriend.ui.fooriendicon.Fooriendicon
import com.team13.fooriend.ui.fooriendicon.Like
import com.team13.fooriend.ui.fooriendicon.Verified
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.ApiService
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
    onWriteReviewClick: (String, String) -> Unit,
    onWriterClick: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Retrofit 설정
    val retrofit = createRetrofit(context)

    val apiService = retrofit.create(ApiService::class.java)
    var resPlaceId = restaurantPlaceId.substring(0,27)
    var restaurantName  by remember {mutableStateOf(restaurantPlaceId.substring(27))}
    var restaurantGood by remember { mutableStateOf(0) }
    var restaurantBad by remember { mutableStateOf(0) }
    var flag by remember { mutableStateOf(0) }
    var userProfileImageUrls by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    fun changeFlag(num: Int){
        if(flag != num) flag = num
        else flag = 0
    }

    LaunchedEffect(flag){
        val response = apiService.getRestaurantDetail(restaurantPlaceId = resPlaceId)
        reviews = response.reviewList
        if(flag == 1){
            //reviews that are positive
            reviews = reviews.filter { it.isPositive }
        } else if(flag == 2){
            //reviews that are negative
            reviews = reviews.filter { !it.isPositive }
        }
    }

    // LaunchedEffect를 사용하여 Composable이 처음 구성될 때 데이터 로드
    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getRestaurantDetail(restaurantPlaceId = resPlaceId)
            reviews = response.reviewList
            for(review in reviews){
                if(review.isPositive){
                    restaurantGood += 1
                }
                else{
                    restaurantBad += 1
                }
                val user = apiService.getUserDetail(userId = review.user.id)
                userProfileImageUrls += mapOf(review.user.id to user.profileImage)

            }
            if(reviews.isNotEmpty() && restaurantName == "") restaurantName = reviews[0].restaurant.name
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
                    onPosClick = { changeFlag(1) },
                    onNegClick = { changeFlag(2) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
//                    .padding(16.dp),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(reviews) { review -> // restaurant에 있는 reveiwList를 가져옴
                    ReviewItem(
                        review = review,
                        onWriterClick = onWriterClick,
                        flag = 0,
                        userProfileImageUrl = userProfileImageUrls[review.user.id] ?: ""
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review, onWriterClick: (Int) -> Unit, flag: Int, userProfileImageUrl: String) {
    val image = review.images
    val isReceiptVerified = review.receiptImage?.isReceiptVerified ?: false
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onWriterClick(review.user.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .width(30.dp)
                    .height(15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = userProfileImageUrl,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop,
                    )

                    Text(
                        text = review.user.name,
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterStart),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 0.dp, 10.dp),
        ){
            Text(text = review.content)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (review.isPositive) {
                Icon(
                    imageVector = FooriendIcon.Like,
                    contentDescription = "Positive Review",
                    modifier = Modifier.size(12.dp)

                )
            } else if (!review.isPositive) {
                Icon(
                    imageVector = FooriendIcon.Dislike,
                    contentDescription = "Negative Review",
                    modifier = Modifier.size(12.dp)
                )
            }

            if (review.receiptImage?.isReceiptVerified == true) {
                Icon(
                    imageVector = FooriendIcon.Verified,
                    contentDescription = "Receipt Verified",
                    modifier = Modifier.size(12.dp)
                )
            }
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
    onWriteReviewClick: (String, String) -> Unit,
    restaurantPlaceId: String,
    restaurantName: String,
    restaurantGood: Int,
    restaurantBad: Int,
    onPosClick: () -> Unit,
    onNegClick: () -> Unit,
) {
    var isLikeSelected by remember { mutableStateOf(true) }
    var isDislikeSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp),
//            .background(color = Color.White)
//            .padding(8.dp, 8.dp, 8.dp, 16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close"
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = FooriendIcon.Fooriendicon,
                    contentDescription = "Store",
                    modifier = Modifier
                        .padding(8.dp).heightIn(min = 15.dp, max = 20.dp).fillMaxHeight()
                        .widthIn(min = 15.dp, max = 20.dp).fillMaxWidth()
                )
                if (restaurantName != null) {
                    Text(
                        text = restaurantName,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                    )
                }
            }
        }

        Button(
            onClick = { onWriteReviewClick(restaurantPlaceId, restaurantName) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(1.dp, Color.Black)

        ) {
            Text(text = "리뷰 작성하러 가기")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        onPosClick()
                        isLikeSelected = true
                        isDislikeSelected = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLikeSelected) FooriendColor.FooriendGreen else Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 60.dp)
                ) {
                    Text(text = "좋아요 $restaurantGood")
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        onNegClick()
                        isDislikeSelected = true
                        isLikeSelected = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDislikeSelected) FooriendColor.FooriendRed else Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 60.dp)
                ) {
                    Text(text = "싫어요 $restaurantBad")
                }
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
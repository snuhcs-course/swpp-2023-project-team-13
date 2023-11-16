package com.team13.fooriend.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.createRetrofit
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ReviewDetailScreen(
    context: Context,
    reviewId: Int,
    onBackClick: () -> Unit,
    onWriterClick: (Int) -> Unit,
    onRestaurantClick: (String) -> Unit,
) {
    val retrofit = createRetrofit(context)

    val apiService = retrofit.create(ApiService::class.java)
    //add review variable
    var review by remember { mutableStateOf<Review?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            Log.d("RestaurantDetailScreen", "reviewId: $reviewId")
            review = apiService.getReviewDetail(reviewId = reviewId)
            Log.d("RestaurantDetailScreen", "review: $review")
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        isLoading = false
    }
    val scrollState = rememberScrollState()

    if(!isLoading){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                IconButton(onClick = {
                    coroutineScope.launch{
                        apiService.deleteReview(reviewId)
                        Toast.makeText(context, "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(onClick = { onWriterClick(review!!.user.id) }) {
                    Text(text = "Writer")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = { onRestaurantClick(review!!.restaurant.googleMapPlaceId) }) {
                    Text(text = "Restaurant")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                LazyRow() {
                    review?.let {
                        items(it.images) {
                            Image(
                                painter = rememberImagePainter(
                                    data = it.url,
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
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                review?.let {
                    Text(
                        text = it.content,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ReviewDetailScreenPreview() {
    ReviewDetailScreen(
        context = TODO(),
        reviewId = 0,
        onBackClick = {},
        onWriterClick = {},
        onRestaurantClick = {},
    )
}

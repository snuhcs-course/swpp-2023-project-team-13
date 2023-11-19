package com.team13.fooriend.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.ui.FooriendIcon
import com.team13.fooriend.ui.fooriendicon.Dislike
import com.team13.fooriend.ui.fooriendicon.Fooriendicon
import com.team13.fooriend.ui.fooriendicon.Like
import com.team13.fooriend.ui.fooriendicon.Verified
import com.team13.fooriend.ui.theme.notosanskr
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
    var myId by remember { mutableStateOf(0) }
    //add review variable
    var review by remember { mutableStateOf<Review?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var userProfileImageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            review = apiService.getReviewDetail(reviewId = reviewId)
            myId = apiService.getMyInfo().id
            var User = apiService.getUserDetail(userId = myId)
            userProfileImageUrl = User.profileImage
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        isLoading = false
    }
    val scrollState = rememberScrollState()

    if(!isLoading){
        Column(
            modifier = Modifier
                .padding(top = 7.dp)
                .fillMaxSize(),

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
                if(review!!.user.id == myId) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            apiService.deleteReview(reviewId)
                            Toast.makeText(context, "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onWriterClick(review!!.user.id) }
                        .wrapContentWidth(Alignment.Start)
                        .padding(end = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .heightIn(min = 15.dp, max = 20.dp)
                                .fillMaxHeight()
                                .widthIn(min = 15.dp, max = 20.dp)
                                .fillMaxWidth()
                                .graphicsLayer {
                                    clip = true
                                    shape = CircleShape
                                }
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = userProfileImageUrl,
                                    builder = {
                                        crossfade(true)
                                    }),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        ClickableText(
                            text = AnnotatedString(review!!.user.name),
                            onClick = { offset ->
                                onWriterClick(review!!.user.id)
                            },
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.notosanskr_regular, FontWeight.Normal))
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .clickable { onRestaurantClick(review!!.restaurant.googleMapPlaceId) }
                        .wrapContentWidth(Alignment.Start)
                        .padding(start = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .heightIn(min = 15.dp, max = 20.dp)
                                .fillMaxHeight()
                                .widthIn(min = 15.dp, max = 20.dp)
                                .fillMaxWidth()
                        )
                        {Icon(
                            imageVector = FooriendIcon.Fooriendicon,
                            contentDescription = "Your Icon Description",
                            modifier = Modifier.size(12.dp)
                        )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        ClickableText(
                            text = AnnotatedString(review!!.restaurant.name),
                            onClick = { offset ->
                                onRestaurantClick(review!!.restaurant.googleMapPlaceId)
                            },
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.notosanskr_bold, FontWeight.Bold))
                            )
                        )
                    }

                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
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
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (review?.isPositive == true) {
                            Icon(
                                imageVector = FooriendIcon.Like,
                                contentDescription = "Positive Review",
                                modifier = Modifier.size(12.dp)
                            )
                        } else if (!review?.isPositive!!) {
                            Icon(
                                imageVector = FooriendIcon.Dislike,
                                contentDescription = "Negative Review",
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        if (review!!.receiptImage?.isReceiptVerified == true) {
                            Icon(
                                imageVector = FooriendIcon.Verified,
                                contentDescription = "Receipt Verified",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(10.dp))
                review?.let {
                    Text(
                        text = it.content,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = notosanskr,
                        fontSize = 16.sp,

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

package com.team13.fooriend.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.data.Review
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    onBackClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onWriterClick: (Int) -> Unit,
) {
    Scaffold(
        // top bar를 설정하면서 top bar 영역은 scroll의 영향을 받지 않도록 한다.
        topBar = {
            TopRestaurantBar(
                onCloseClick = onBackClick,
                onWriteReviewClick = onWriteReviewClick,
                restaurant = restaurant,
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
        ){
            items(restaurant.reviewList) { reviewId -> // restaurant에 있는 reveiwList를 가져온다.
                ReviewItem(
                    reviewId = reviewId,
                    onWriterClick = onWriterClick,
                )
            }
        }
    }
}

@Composable
fun ReviewItem(reviewId: Int, onWriterClick: (Int) -> Unit) {
    // 실제로는 reviewId를 가지고 서버에서 받아와야 함
    val review1 = Review(id = 1, writerId = 1, restaurantId = 1, content = "탕수육이 진짜 바삭!!, 여기 진짜 짬뽕 맛집이예요 별점 10개도 부족합니다.",
        image = listOf(R.drawable.tangsuyug, R.drawable.jjambbong, R.drawable.jjambbong),
        confirm = true, title = "title")
    val review2 = Review(id = 2, writerId = 1, restaurantId = 1, content = "이 집 짜장이 기가 막히네",
        image = listOf(R.drawable.jjajangmyeon),
        confirm = true, title = "title")
    val review3 = Review(id = 3, writerId = 1, restaurantId = 1, content = "이 집 고양이 때문에 심장이 너무 아팠습니다.. ㅠㅠ",
        image = listOf(R.drawable.profile_cat),
        confirm = true, title = "title")
    // reviewId에 따라 다른 review를 가져온다.
    var review: Review
    if (reviewId == 1) review = review1
    else if (reviewId == 2) review = review2
    else review = review3
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
            items(review.image){
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
        IconButton(onClick = { onWriterClick(review.writerId) }) {
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
            Text(text = review.title)
            Text(text = review.content)
        }
    }
}

@Composable
fun TopRestaurantBar(
    onCloseClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    restaurant: Restaurant,
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
        Text(text = restaurant.name)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            TextButton(onClick = { /*TODO*/ }) {// 좋아요 버튼 누르면 긍정 리뷰만 뜨도록
                Text(text = "좋아요 ${restaurant.good}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = { /*TODO*/ }) {// 싫어요 버튼 누르면 부정 리뷰만 뜨도록
                Text(text = "싫어요 ${restaurant.bad}")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = onWriteReviewClick) {
                Text(text = "리뷰 작성")
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun RestaurantDetailScreenPreview() {
    RestaurantDetailScreen(
        restaurant = Restaurant(
            id = 0,
            name = "음식점 이름",
            good = 0,
            bad = 0,
            reviewList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
            latitude = 0.0,
            longitude = 0.0,
            reviewCount = 10,
        ),
        onBackClick = {},
        onWriteReviewClick = {},
        onWriterClick = {},
    )
}
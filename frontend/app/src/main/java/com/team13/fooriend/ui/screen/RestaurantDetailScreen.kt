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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    onCloseClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onWriterClick: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopRestaurantBar(
                onCloseClick = onCloseClick,
                onWriteReviewClick = onWriteReviewClick,
                restaurant = restaurant,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ){
            item {
                Text(text = "리뷰")
            }
            items(restaurant.reviewList) { reviewId ->
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
    val review = Review(
        id = reviewId,
        restaurantId = 0,
        writerId = 0,
        title = "title",
        content = "짜장면이 맛있어요!",
        confirm = true,
        image = listOf(R.drawable.hamburger, R.drawable.profile_cat, R.drawable.hamburger)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White)
            .padding(10.dp, 5.dp)
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
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Writer",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp)
                .align(Alignment.BottomStart)
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
            .padding(10.dp, 5.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ){
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = restaurant.name)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(text = "좋아요 ${restaurant.good}")
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "싫어요 ${restaurant.bad}")
            Button(onClick = onWriteReviewClick) {
                Text(text = "리뷰 작성")
            }
        }
    }
}
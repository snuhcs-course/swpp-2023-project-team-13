package com.team13.fooriend.ui.screen

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.Restaurant
import com.team13.fooriend.data.Review

@Composable
fun ReviewDetailScreen(
    reviewId: Int,
    onBackClick: () -> Unit,
    onWriterClick: (Int) -> Unit,
    onRestaurantClick: (Int) -> Unit,
) {
    // review 예시 코드, 실제는 reviewId  값을 가지고 서버에서 받아와야 함
    val review = Review(
        id = reviewId,
        restaurantId = 0,
        writerId = 0,
        title = "title",
        content = "content",
        confirm = true,
        image = listOf(R.drawable.hamburger, R.drawable.profile_cat, R.drawable.hamburger)
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        IconButton(onClick = onBackClick) {
           Icon(
               imageVector = Icons.Default.ArrowBack,
               contentDescription = "Back",
               tint = Color.Black
           )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ){
            Button(onClick = { onWriterClick(review.writerId) }) {
                Text(text = "Writer")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = { onRestaurantClick(review.restaurantId) }){
                Text(text = "Restaurant")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            LazyRow(){
                items(review.image){
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "review image",
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = review.content,
                modifier = Modifier.fillMaxWidth(),
            )
        }

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ReviewDetailScreenPreview() {
    ReviewDetailScreen(
        reviewId = 0,
        onBackClick = {},
        onWriterClick = {},
        onRestaurantClick = {},
    )
}

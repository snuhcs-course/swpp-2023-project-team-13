package com.team13.fooriend.ui.screen.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.Review


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    // reviews: List<Review> 파라미터로 받아야 하나?
    onReviewClick : (Int) -> Unit,
){
    // review 예시 코드, 실제는 List에 있는 review들의 id 값을 가지고 서버에서 받아와야 함
    var reviews = List<Review>(10) { index ->
        Review(
            id = index,
            restaurantId = index,
            writerId = index,
            title = "title",
            content = "content",
            confirm = true,
            image = listOf(R.drawable.hamburger, R.drawable.profile_cat, R.drawable.profile_cat)
        )
    }
    val (search, searchValue) = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = search, onValueChange = searchValue)
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ){
            // review들이 사용자 위치기반으로 가까운 리뷰들 노출하는건 어떨까?
            items(
                items = reviews,
                key = { review -> review.id }
            ){ review ->
                ReviewCard(review, onClick = { onReviewClick(review.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCard(
    review: Review,
    onClick: () -> Unit = {  },
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ){
        Box(
            modifier = Modifier.height(150.dp),
        ){
            Image(
                painter = painterResource(id = review.image[0]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )


        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun SocialScreenPreview(){
    SocialScreen(onReviewClick = {})
}
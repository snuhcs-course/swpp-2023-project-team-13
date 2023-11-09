package com.team13.fooriend.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.Review

@Composable
fun ReviewLazyGrid(
    reviews: List<Review> = listOf(),
    onReviewClick : (Int) -> Unit, // 리뷰 이미지를 클릭한 경우
    verticalPadding : Int = 16,
    horizontalPadding : Int = 4,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(verticalPadding.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCard(
    review: Review,
    onClick: () -> Unit = {  },
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.height(150.dp),
        ) {
            Image(
                painter = rememberImagePainter(
                    data = review.images[0].url,
                    builder = {
                        crossfade(true)
                    },
                ),
                contentDescription = "Loaded image",
                contentScale = ContentScale.Crop
            )
        }
    }

}
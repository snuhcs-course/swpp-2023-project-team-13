package com.team13.fooriend.ui.screen.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.Review
import com.team13.fooriend.data.User
import com.team13.fooriend.ui.component.ReviewLazyGrid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    // reviews: List<Review> 파라미터로 받아야 하나?
    onReviewClick : (Int) -> Unit, // 리뷰 이미지를 클릭한 경우
    onUserClick : (Int) -> Unit, // search bar에서 검색한 유저를 클릭한 경우
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        SocialSearchBar(onUserClick = onUserClick)
        Spacer(modifier = Modifier.height(16.dp))
        ReviewLazyGrid(/* reviews = reviews, */ onReviewClick = onReviewClick)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialSearchBar(
    onUserClick: (Int) -> Unit
) {
    var text by remember { mutableStateOf("Search") }
    var active by remember { mutableStateOf(false) }
    var items = remember{ mutableListOf("yongchan")}
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            items.add(text)
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if(active) {
                Icon(
                    modifier = Modifier.clickable {
                        if(text.isNotEmpty()){
                            text = ""
                        }else{
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        }
    ) {
        items.forEach{
            Row(modifier = Modifier.padding(all = 0.dp)){
                Icon(
                    modifier = Modifier.padding(all = 12.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "History Icon"
                )
                TextButton(
                    onClick = { onUserClick(0) },
                ) {// user id를 파라미터로 넘겨야 한다.
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}



@Composable
@Preview(showSystemUi = true, showBackground = true)
fun SocialScreenPreview(){
    SocialScreen(onReviewClick = {}, onUserClick = {})
}
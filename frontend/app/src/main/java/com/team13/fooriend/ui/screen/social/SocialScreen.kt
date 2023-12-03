package com.team13.fooriend.ui.screen.social

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.team13.fooriend.R
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.AbstractUser
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.ReviewsListSaver
import com.team13.fooriend.ui.util.User
import com.team13.fooriend.ui.util.createRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    context: Context,
    onReviewClick : (Int) -> Unit, // 리뷰 이미지를 클릭한 경우
    onUserClick : (Int) -> Unit, // search bar에서 검색한 유저를 클릭한 경우
){
    val retrofit = createRetrofit(context)

    val apiService = retrofit.create(ApiService::class.java)

    var reviews by rememberSaveable(stateSaver = ReviewsListSaver) { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    Log.d("SocialScreen", "isLoading: $isLoading")
    if(isLoading) {
        LaunchedEffect(Unit) {
            try {
                // API 호출하여 데이터 가져오기
                reviews = apiService.getRandomReviews().reviewList
            } catch (e: Exception) {
                Log.d("RestaurantDetailScreen", "error: $e")
            }
            isLoading = false
        }
    }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        SocialSearchBar(context = context, onUserClick = onUserClick)
        Spacer(modifier = Modifier.height(16.dp))
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                isLoading = true
            }
        ) {
            ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
        }
        ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialSearchBar(
    context: Context,
    onUserClick: (Int) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember{ mutableListOf("yongchan")} // history log
    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)
    val coroutineScope = rememberCoroutineScope()
    var users by remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(text){
        if (text.isNotEmpty()) {
            // Make the API call and update the users state
            val Abstractusers = apiService.searchUser(text).userList
            var tmp = emptyList<User>()
            for (user in Abstractusers) {
                tmp += apiService.getUserDetail(user.id)
            }
            users = tmp
        } else{
            users = emptyList()
        }
    }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        shape = RectangleShape,
        colors = SearchBarDefaults.colors(Color.LightGray),
        onSearch = {// 일반 검색을 통해서는 유저 프로필 검색 불가 like instagram
//            items.add(text)
//            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "친구를 이름으로 검색", fontSize = 15.sp)
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

        // text가 empty인 경우 history를 보여준다.
//        if(text.isEmpty()) {
//            items.forEach {
//                Row(modifier = Modifier.padding(all = 0.dp)) {
//                    Icon(
//                        modifier = Modifier.padding(all = 12.dp),
//                        imageVector = Icons.Default.History,
//                        contentDescription = "History Icon"
//                    )
//                    TextButton(
//                        onClick = { onUserClick(0) },
//                    ) {// user id를 파라미터로 넘겨야 한다.
//                        Text(
//                            text = it,
//                            modifier = Modifier.fillMaxWidth(0.85f),
//                            textAlign = TextAlign.Start,
//                        )
//                    }
//                    IconButton(onClick = { items.remove(it)}) {
//                        Icon(imageVector = Icons.Default.Close, contentDescription = "delete history")
//                    }
//                }
//            }
        users.forEach{
            UserRow(user = it, onUserClick = onUserClick)
        }
    }
}

@Composable
fun UserRow(user: User, onUserClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.id) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(user.profileImage),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = user.name,
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = TextAlign.Start,
        )
    }
}



@Composable
@Preview(showSystemUi = true, showBackground = true)
fun SocialScreenPreview(){
    SocialScreen(context = TODO(),onReviewClick = {}, onUserClick = {})
}
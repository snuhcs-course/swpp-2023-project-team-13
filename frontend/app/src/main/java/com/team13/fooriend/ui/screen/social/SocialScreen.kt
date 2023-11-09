package com.team13.fooriend.ui.screen.social

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.R
import com.team13.fooriend.data.User
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    // reviews: List<Review> 파라미터로 받아야 하나?
    onReviewClick : (Int) -> Unit, // 리뷰 이미지를 클릭한 경우
    onUserClick : (Int) -> Unit, // search bar에서 검색한 유저를 클릭한 경우
){
    // 임시로 id=1 호출
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    var reviews by rememberSaveable { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        SocialSearchBar(onUserClick = onUserClick)
        Spacer(modifier = Modifier.height(16.dp))
        ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialSearchBar(
    onUserClick: (Int) -> Unit,
    historyLog: List<String> = listOf("yongchan", "philipp", "apple"),
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember{ mutableListOf("yongchan")} // history log
    // user는 db에 있는 전체 유저 데이터를 가져와야 한다
    val users by remember{ mutableStateOf(
        listOf(
            User(1, "John Smith", "abc", "123"),
            User(2, "Mary Johnson", "abc", "123"),
            User(3, "David Davis", "abc", "123"),
            User(4, "Linda Wilson", "abc", "123"),
            User(5, "Michael Jones", "abc", "123"),
            User(6, "Sarah Miller", "abc", "123"),
            User(7, "Robert Brown", "abc", "123"),
            User(8, "Karen Lee", "abc", "123"),
            User(9, "William Clark", "abc", "123"),
            User(10, "Jennifer Hall", "abc", "123"),
        )
    )}
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {// 일반 검색을 통해서는 유저 프로필 검색 불가 like instagram
//            items.add(text)
//            active = false
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
        // text가 empty인 경우 history를 보여준다.
        if(text.isEmpty()) {
            items.forEach {
                Row(modifier = Modifier.padding(all = 0.dp)) {
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
                            modifier = Modifier.fillMaxWidth(0.85f),
                            textAlign = TextAlign.Start,
                        )
                    }
                    IconButton(onClick = { items.remove(it)}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "delete history")
                    }
                }
            }
        }else{
            // text가 empty가 아닌 경우 검색 결과를 보여준다.
            // users에서 filter를 통해 검색 결과를 보여준다.
            val _users = users.filter { it.name.contains(text, ignoreCase = true) }
            _users.forEach{
                Row(modifier = Modifier.padding(all = 0.dp)) {
                    Icon(
                        modifier = Modifier.padding(all = 12.dp),
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account Icon"
                    )
                    TextButton(
                        onClick = {
                            items.add(it.name) // add는 되는데 stack 이 구현되기 때문에 과거의 social screen으로 돌아온다.
                            onUserClick(0)
                        }
                    ) {// user id를 파라미터로 넘겨야 한다.
                        Text(
                            text = it.name,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                        )
                    }
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
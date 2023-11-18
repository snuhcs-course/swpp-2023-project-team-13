package com.team13.fooriend.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.team13.fooriend.ui.component.ProfileSection
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.screen.mypage.UserList
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.User
import com.team13.fooriend.ui.util.createRetrofit

@Composable
fun FooriendScreen(
    context: Context,
    onBackClick: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    userId: Int = 0,
    onReviewClick: (Int) -> Unit = {},
    onUserClick: (Int) -> Unit,
){
    val retrofit = createRetrofit(context)

    val apiService = retrofit.create(ApiService::class.java)

    var fName by remember { mutableStateOf("") }
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var followerList by remember { mutableStateOf<List<Int>>(emptyList()) }
    var followingList by remember { mutableStateOf<List<Int>>(emptyList()) }
    var followers by remember { mutableStateOf<List<User>>(emptyList()) }
    var followings by remember { mutableStateOf<List<User>>(emptyList()) }
    var showPopup by remember { mutableStateOf(false) }
    var clickedFollower by remember { mutableStateOf(false) }
    fun changePopupState(bool: Boolean){
        clickedFollower = bool
        showPopup = !showPopup
    }
    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getUserReviews(userId = userId)
            var user = apiService.getUserDetail(userId = userId)
            reviews = response.reviewList
            followerList = apiService.getFollows(userId).followers.map {
                it.id
            }
            fName = user.name
            for(id in followerList){
                followers += apiService.getUserDetail(id)
            }
            followingList = apiService.getFollows(userId).followings.map{
                it.id
            }
            for(id in followingList){
                followings += apiService.getUserDetail(id)
            }
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        isLoading = false
    }

    Column(
//        modifier = Modifier.padding(16.dp)
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 7.dp)
                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "back")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "face",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = fName,
                    style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                )
            }
        }

        // profile section
        if(isLoading) {
            Text(text = "Loading...")
        } else {
            ProfileSection(context = context,
                userId = userId,
                onFollowClick = onFollowClick,
                clickedFollower = { changePopupState(true) },
                clickedFollowing = { changePopupState(false) },
                )
            if (showPopup) {
                Popup(
                    alignment = Alignment.Center,
                    onDismissRequest = { showPopup = false }
                ) {
                    // Your popup content
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                    ) {
                        Column {
                            // Tabs for Followers and Following
                            var selectedTab by remember { mutableStateOf(if (clickedFollower) 0 else 1) }
                            TabRow(selectedTabIndex = selectedTab) {
                                Tab(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    text = { Text("Followers") }
                                )
                                Tab(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    text = { Text("Following") }
                                )
                            }

                            // List content
                            when (selectedTab) {
                                0 -> UserList(userList = followers, onUserClick = onUserClick)
                                1 -> UserList(userList = followings, onUserClick = onUserClick)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
        }
        // review lazy grid
    }
}

//@Composable
//@Preview(showSystemUi = true, showBackground = true)
//fun FooriendScreenPreview(){
//    FooriendScreen(TODO())
//}

